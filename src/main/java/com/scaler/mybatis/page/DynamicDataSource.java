package com.scaler.mybatis.page;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
	public static final String DATA_SOURCE_DQC = "dqcDataSource";

    public static final String DATA_SOURCE_BAM = "bamDataSource";
    
    public static final String DATA_SOURCE_EXTRACT = "dqextractdataSource";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType) {

            contextHolder.set(customerType);

    }

    public static String getCustomerType() {

            return contextHolder.get();

    }

    public static void clearCustomerType() {

            contextHolder.remove();

    }
	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
	    return getCustomerType();
	}

}
