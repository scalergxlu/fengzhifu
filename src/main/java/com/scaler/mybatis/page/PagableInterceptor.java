package com.scaler.mybatis.page;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

@Intercepts({
		@Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }),
		@Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class PagableInterceptor implements Interceptor {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof StatementHandler) {
			return prepare(invocation);
		}
		Object resultObj = invocation.proceed();
		Pagination<?> pagination = findPaginationParam(invocation.getArgs()[1]);
		if (pagination == null) {
			if (logger.isInfoEnabled()) {
				logger.info("当前执行sql[ "
						+ ((MappedStatement) (invocation.getArgs()[0])).getBoundSql(invocation.getArgs()[1]).getSql()
								.replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("\\s{1,}", " ") + " ]的结果："
						+ resultObj.toString());
			}
			return resultObj;
		}

		if (resultObj instanceof List) {
			pagination.setList((List) resultObj);
		}
		pagination.afterQuery();
		List<Pagination<?>> resultList = new ArrayList<Pagination<?>>();
		resultList.add(pagination);
		return resultList;
	}

	protected Object prepare(Invocation invocation) throws Throwable {
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + "分页查询拦截器启动...");
		}
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) ReflectHelper.getFieldValue(handler, "delegate");
		BoundSql boundSql = delegate.getBoundSql();
		Object paramObj = boundSql.getParameterObject();
		if (logger.isInfoEnabled()) {
			logger.info("当前正在执行的sql语句："
					+ boundSql.getSql().replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("\\s{1,}", " ") + "，参数："
					+ String.valueOf(paramObj));
		}
		Pagination<?> pagination = findPaginationParam(paramObj);
		if (pagination == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("当前查询参数中没有：" + Pagination.class.getName() + "类型的参数，不进行分页查询拦截");
			}
			return invocation.proceed();
		}
		Connection connection = (Connection) invocation.getArgs()[0];
		setTotalPages(delegate, connection, paramObj, pagination);
		buildPaginationSql(boundSql, pagination);
		return invocation.proceed();
	}

	protected void buildPaginationSql(BoundSql boundSql, Pagination<?> pagination) {
		String paginationSql = DialectFactory.getDialect(DialectType.MYSQL).buildPaginationSql(boundSql.getSql(),
				pagination.getOffset(), pagination.pageSize);
		if (logger.isInfoEnabled()) {
			logger.info("分页查询拦截器构建的分页sql语句："
					+ paginationSql.replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("\\s{1,}", " "));
		}
		ReflectHelper.setFieldValue(boundSql, "sql", paginationSql);
	}

	protected void setTotalPages(StatementHandler delegate, Connection connection, Object paramObj,
			Pagination<?> pagination) throws SQLException {
		MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getFieldValue(delegate, "mappedStatement");
		BoundSql boundSql = mappedStatement.getBoundSql(paramObj);
		String getCountSql = buildGetCountSql(boundSql);
		if (logger.isInfoEnabled()) {
			logger.info("分页查询拦截器构建的查询记录总数的sql语句："
					+ getCountSql.replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("\\s{1,}", " "));
		}
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), getCountSql, parameterMappings,
				paramObj);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, paramObj, countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(getCountSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pagination.setTotalRecords(rs.getInt(1));
			}
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("关闭ResultSet时异常.", e);
					}
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("关闭PreparedStatement时异常.", e);
					}
				}
		}
	}

	protected String buildGetCountSql(BoundSql boundSql) {
		return "select count(1) from ( " + boundSql.getSql() + " )a";
	}

	protected Pagination<?> findPaginationParam(Object paramObj) {
		if (paramObj instanceof Pagination<?>)
			return (Pagination<?>) paramObj;
		if (paramObj instanceof Map) {
			for (Entry<?, ?> entry : (((Map<?, ?>) paramObj).entrySet())) {
				if (entry.getValue() instanceof Pagination<?>)
					return (Pagination<?>) entry.getValue();
			}
		}
		return null;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

}
