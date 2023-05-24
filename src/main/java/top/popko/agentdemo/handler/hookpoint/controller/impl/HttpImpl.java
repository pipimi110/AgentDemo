package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpImpl {
    private static final ThreadLocal<Map<String, Object>> REQUEST_META = new ThreadLocal();

    public HttpImpl() {
    }

    public static String getRequestInfo(Object request,String methodName){
        try {
            //jetty
            Class clazz = Class.forName("javax.servlet.http.HttpServletRequestWrapper",false,request.getClass().getClassLoader());
            if(!clazz.isInstance(request)){
                clazz = request.getClass();//spring
            }
            return (String) clazz.getDeclaredMethod("getRequestURI").invoke(request);
        } catch (Exception e) {
            return "[error]HttpImpl.getRequestInfo()";
        }
    }
    public static Map<String, Object> getRequestMeta(Object request) {
        Map<String, Object> requestMeta = new HashMap(2);
        requestMeta.put("requestURI", getRequestInfo(request,"getRequestURI"));
        return requestMeta;
    }

    public static void solveHttp(MethodEvent event) {
        System.out.println("[+]solveHttp");
        try {
            REQUEST_META.set(getRequestMeta(event.parameterInstances[0]));
        } catch (Exception e) {
        }

        //后缀/黑名单过滤
        System.out.println("requestURI:");
        System.out.println(REQUEST_META.get().get("requestURI"));// /vuldemo /favicon.ico /error
        if (!ConfigMatcher.getInstance().disableExtension((String)((Map)REQUEST_META.get()).get("requestURI"))) {
//            if (!ConfigMatcher.getInstance().getBlackUrl((Map)REQUEST_META.get())) {
                EngineManager.enterHttpEntry((Map)REQUEST_META.get());
//            }
        }
    }
}

