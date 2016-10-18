package com.scaler.auth;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.scaler.auth.vo.AppUser;
import com.scaler.db.DBManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
* token 校验类
* */
public class AppKeyManager
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static AppKeyManager instance = new AppKeyManager();
    private Map<String, AppUser> appUserCache = new ConcurrentHashMap();

    private AppKeyManager()
    {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    for (AppUser appUser : appUserCache.values()) {
                        if (System.currentTimeMillis() - appUser.getLoadTime() >= 180000L)
                            appUserCache.remove(appUser);
                    }
                    try
                    {
                        sleep(10000L);
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                }
            }
        };
        thread.start();
    }

    public static AppKeyManager getInstance() {
        return instance;
    }

    public AppUser getAppUser(String appId) {
        AppUser appUser = (AppUser)this.appUserCache.get(appId);
        QueryRunner qr = DBManager.getInstance().getQueryRunner();
        if (appUser == null) {
            try {
                appUser = (AppUser)qr.query("select * from cl_appuser where app_id=?", new BeanHandler(AppUser.class), new Object[] { appId });
                if (appUser != null) {
                    appUser.setLoadTime(System.currentTimeMillis());
                    this.appUserCache.put(appId, appUser);
                }
            } catch (Exception e) {
                this.logger.error("", e);
            }
        }
        return appUser;
    }

    public boolean callOperation(String uri, String appId) {
        QueryRunner qr = DBManager.getInstance().getQueryRunner();
        try {
            Map o = (Map)qr.query("select s.id from cl_appusestat s,cl_appuser u where appuser_id=u.id and u.app_id=? and api_uri=? and use_count<=use_limit", new MapHandler(), new Object[] { appId, uri });
            if (o != null) {
                Long id = (Long)o.get("id");
                qr.update("update cl_appusestat set use_count=use_count+1 where id=?", id);
                return true;
            }
        }
        catch (SQLException e) {
            this.logger.error("", e);
        }
        return false;
    }
}