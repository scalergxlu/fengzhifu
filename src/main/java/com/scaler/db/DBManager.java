package com.scaler.db;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
/*
* 简易jdbc
* */
public class DBManager
{
    protected Logger logger = Logger.getLogger(getClass());
    private static DBManager instance = new DBManager();
    private Properties prop = new Properties();
    private DataSource ds;

    private DBManager()
    {
        InputStream in = DBManager.class.getResourceAsStream("/jdbc.properties");
        try {
            this.prop.load(in);
            Map map = new HashMap();
            for (Iterator i$ = this.prop.keySet().iterator(); i$.hasNext(); ) { Object k = i$.next();
                String key = String.valueOf(k);
                String value = this.prop.getProperty(key).trim();
                if (key.startsWith("connection.")) {
                    key = key.substring(11);
                    map.put(key, value);
                }
            }
            this.ds = DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            this.logger.error("", e);
        }
    }

    public static DBManager getInstance() {
        return instance;
    }

    public DataSource getDatasource() {
        return this.ds;
    }

    public QueryRunner getQueryRunner() {
        return new QueryRunner(this.ds);
    }
}