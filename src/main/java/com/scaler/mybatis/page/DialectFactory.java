package com.scaler.mybatis.page;

public class DialectFactory {

	public static Dialect getDialect(DialectType dialectType) {

		switch (dialectType) {
		case MYSQL:
			return new MysqlDialect();
		case ORACLE:
			return new OracleDialect();
		default:
			return new OracleDialect();
		}

	}
}
