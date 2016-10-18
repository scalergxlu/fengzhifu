package com.scaler.auth.vo;

public class AppUser
{
    private String app_id;
    private String app_name;
    private String app_key;
    private String app_desc;
    private String access_rule;
    private long loadTime;

    public String getApp_id()
    {
        return this.app_id;
    }
    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
    public String getApp_name() {
        return this.app_name;
    }
    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }
    public String getApp_key() {
        return this.app_key;
    }
    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }
    public String getApp_desc() {
        return this.app_desc;
    }
    public void setApp_desc(String app_desc) {
        this.app_desc = app_desc;
    }
    public String getAccess_rule() {
        return this.access_rule;
    }
    public void setAccess_rule(String access_rule) {
        this.access_rule = access_rule;
    }
    public long getLoadTime() {
        return this.loadTime;
    }
    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }
}
