package com.scaler.mybatis.page;

public interface Dialect {

	String buildPaginationSql(String originalSql, int offset, int length);

}
