package com.scaler.auth;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
/*
* token通用类
* */
public class SignUtil
{
    private static Logger logger = Logger.getLogger(SignUtil.class);

    public static String getSignInfo(Map map, String encryptKey)
    {
        Map sortedMap = new TreeMap(map);
        String url = "";
        for (Iterator iterator = sortedMap.entrySet().iterator(); iterator.hasNext(); )
        {
            Object obj = iterator.next();
            Map.Entry entry = (Map.Entry)obj;
            Object k = entry.getKey();
            Object v = entry.getValue();
            if ((null != v) && (!"".equals(v)) && (!"signinfo".equals(k)) && (!"key".equals(k))) {
                url = url + k + "=" + v + "&";
            }
        }
        url = url + encryptKey;
        logger.info("getting signinfo for [" + url + "]");
        String signinfo = "";
        try {
            signinfo = DigestUtils.md5Hex(url.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }

        logger.info("result: " + signinfo);
        return signinfo;
    }
}