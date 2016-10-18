package com.scaler.mybatis.page;

public class MysqlDialect implements Dialect {


	public String buildPaginationSql(String originalSql, int offset, int length) {
		return new StringBuilder(originalSql).append(" limit ").append(offset-1)
				.append(",").append(offset+length).toString();
	}

}