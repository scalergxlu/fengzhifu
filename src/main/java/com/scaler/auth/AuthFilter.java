package com.scaler.auth;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.scaler.auth.vo.AppUser;
import com.scaler.jwt.Jwt;
import com.scaler.jwt.TokenState;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
* appkey鉴权校验类
* */
public class AuthFilter implements Filter
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void init(FilterConfig filterConfig)
            throws ServletException
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        response.setCharacterEncoding("UTF8");
        String remoteIP = request.getRemoteAddr();
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        this.logger.info("from " + remoteIP + " to " + httpRequest.getRequestURI());

        Map paramsMap = new HashMap();
        for (Iterator iterator = request.getParameterMap().keySet().iterator(); iterator.hasNext(); )
        {
            Object key = iterator.next();
            String pName = (String)key;
            paramsMap.put(pName, request.getParameter(pName));
        }

//        if (remoteIP.startsWith("127.0.0.1")) {
//            chain.doFilter(httpRequest, response);
//        }else
        if(((HttpServletRequest) request).getRequestURI().endsWith("/login")){
            //登陆接口不校验token，直接放行
            chain.doFilter(request, response);
        }
        else
        {
            //其他API接口一律校验token
            System.out.println("开始校验token");
            //从请求头中获取token
            String token=((HttpServletRequest) request).getHeader("token");
            Map<String, Object> resultMap= Jwt.validToken(token);
            TokenState state= TokenState.getTokenState((String)resultMap.get("state"));
            switch (state) {
                case VALID:
                    //取出payload中数据,放入到request作用域中
                    request.setAttribute("data", resultMap.get("data"));
                    //放行
                    chain.doFilter(request, response);
                    break;
                case EXPIRED:
                case INVALID:
                    System.out.println("无效token");
                    //token过期或者无效，则输出错误信息返回给ajax
                    JSONObject outputMSg=new JSONObject();
                    outputMSg.put("success", false);
                    outputMSg.put("msg", "您的token不合法或者过期了，请重新登陆");
                    PrintWriter out = response.getWriter();
                    out.print(outputMSg);

                    break;
            }

//
//            Map paramsMap = new HashMap();
//            for (Iterator iterator = request.getParameterMap().keySet().iterator(); iterator.hasNext(); )
//            {
//                Object key = iterator.next();
//                String pName = (String)key;
//                paramsMap.put(pName, request.getParameter(pName));
//            }
//            String signInfo = (String)paramsMap.get("signinfo");
//            String appId = (String)paramsMap.get("appid");
//            if (appId != null) {
//                if (appId.equals("kissmyluv!@!")) {
//                    chain.doFilter(httpRequest, response);
//                    return;
//                }
//                AppUser appUser = AppKeyManager.getInstance().getAppUser(appId);
//
//                if ((signInfo != null) && (appId != null) && (appUser != null)) {
//                    String appkey = appUser.getApp_key();
//                    String calcSignInfo = SignUtil.getSignInfo(paramsMap, appkey);
//                    System.out.println(calcSignInfo);
//                    if (calcSignInfo.equals(signInfo)) {
//                        if (AppKeyManager.getInstance().callOperation(httpRequest.getRequestURI(), appId)) {
//                            chain.doFilter(httpRequest, response);
//                            return;
//                        }
//
//                        PrintWriter out = response.getWriter();
//                        out.println(getErrorResult("2001", "call count exceed"));
//                        return;
//                    }
//
//                    PrintWriter out = response.getWriter();
//                    out.println(getErrorResult("2002", "signinfo error"));
//                    return;
//                }
//            }
//            else
//            {
//                PrintWriter out = response.getWriter();
//                out.println(getErrorResult("2003", "appid is null"));
//                return;
//            }
//            PrintWriter out = response.getWriter();
//            out.println(getErrorResult("2004", "no permission to access this website"));
        }
    }

    private String getErrorResult(String code, String message)
    {
        return "{\"errorCode\":\"" + code + "\", \"errorMessage\":\"" + message + "\"}";
    }

    public void destroy()
    {
    }
}