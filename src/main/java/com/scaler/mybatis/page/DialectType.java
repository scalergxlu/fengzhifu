package com.scaler.mybatis.page;

public enum DialectType {

	MYSQL {
		public String getValue() {
			return "MYSQL";
		}
	},
	ORACLE {
		public String getValue() {
			return "ORACLE";
		}
	};
	public String getValue() {
		return null;
	}

}
