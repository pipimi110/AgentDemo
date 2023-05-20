package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.handler.hookpoint.controller.wrapper.ServletRequestWrapper;
//import top.popko.agentdemo.handler.hookpoint.controller.wrapper.ServletResponseWrapper;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpImpl {
    private static Method cloneRequestMethod;
    private static Method cloneResponseMethod;
    private static Class<?> CLASS_OF_SERVLET_PROXY;
//    private static IastClassLoader iastClassLoader;
//    public static File IAST_REQUEST_JAR_PACKAGE = new File(PropertyUtils.getTmpDir() + "dongtai-api.jar");
    private static final ThreadLocal<Map<String, Object>> REQUEST_META = new ThreadLocal();

    public HttpImpl() {
    }

//    private static void createClassLoader(Object req) {
//        try {
//            if (iastClassLoader != null) {
//                return;
//            }
//
//            if (IAST_REQUEST_JAR_PACKAGE.exists()) {
//                iastClassLoader = new IastClassLoader(req.getClass().getClassLoader(), new URL[]{IAST_REQUEST_JAR_PACKAGE.toURI().toURL()});
//                CLASS_OF_SERVLET_PROXY = iastClassLoader.loadClass("io.dongtai.api.ServletProxy");
//                if (CLASS_OF_SERVLET_PROXY == null) {
//                    return;
//                }
//
//                cloneRequestMethod = CLASS_OF_SERVLET_PROXY.getDeclaredMethod("cloneRequest", Object.class, Boolean.TYPE);
//                cloneResponseMethod = CLASS_OF_SERVLET_PROXY.getDeclaredMethod("cloneResponse", Object.class, Boolean.TYPE);
//            }
//        } catch (MalformedURLException var2) {
//            DongTaiLog.error("HttpImpl createClassLoader failed", var2);
//        } catch (NoSuchMethodException var3) {
//            DongTaiLog.error("HttpImpl createClassLoader failed", var3);
//        }
//
//    }

//    private static void loadCloneResponseMethod() {
//        if (cloneResponseMethod == null) {
//            try {
//                if (CLASS_OF_SERVLET_PROXY == null) {
//                    return;
//                }
//
//                cloneResponseMethod = CLASS_OF_SERVLET_PROXY.getDeclaredMethod("cloneResponse", Object.class, Boolean.TYPE);
//            } catch (NoSuchMethodException var1) {
////                DongTaiLog.error(var1);
//            }
//        }
//
//    }
    public static Object cloneRequest(Object request, boolean isJakarta) {
//        return isJakarta ? new JakartaRequestWrapper((HttpServletRequest)request) : new ServletRequestWrapper((javax.servlet.http.HttpServletRequest)request);
        return new ServletRequestWrapper(request);
    }

    public static Object cloneResponse(Object response, boolean isJakarta) {
//        REQUEST_META.remove();
//        return isJakarta ? new JakartaResponseWrapper((HttpServletResponse)response) : new ServletResponseWrapper((javax.servlet.http.HttpServletResponse)response);
//        return new ServletResponseWrapper(response);
        return (response);
    }
//    public static Object cloneRequest(Object req, boolean isJakarta) {
//        if (req == null) {
//            return null;
//        } else {
//            try {
//                if (cloneRequestMethod == null) {
//                    createClassLoader(req);
//                }
//
//                return cloneRequestMethod.invoke((Object)null, req, isJakarta);
//            } catch (IllegalAccessException var3) {
//                return req;
//            } catch (InvocationTargetException var4) {
//                return req;
//            }
//        }
//    }

//    public static Object cloneResponse(Object response, boolean isJakarta) {
//        Object var2;
//        try {
//            Object var3;
//            try {
//                if (response == null) {
//                    return null;
//                }
//
//                if (!ScopeManager.SCOPE_TRACKER.getHttpEntryScope().in()) {
//                    return response;
//                }
//
//                if (!ConfigMatcher.getInstance().disableExtension((String)((Map)REQUEST_META.get()).get("requestURI"))) {
//                    if (ConfigMatcher.getInstance().getBlackUrl((Map)REQUEST_META.get())) {
//                        return response;
//                    }
//
//                    if (cloneResponseMethod == null) {
//                        loadCloneResponseMethod();
//                    }
//
//                    return cloneResponseMethod.invoke(null, response, isJakarta);
////                }
//
//                var2 = response;
//            } catch (IllegalAccessException var8) {
//                var3 = response;
//                return var3;
//            } catch (InvocationTargetException var9) {
//                var3 = response;
//                return var3;
//            }
//        } finally {
//            REQUEST_META.remove();
//        }
//
//        return var2;
//    }

    public static String getRequestInfo(Object request,String methodName){
        try {
            return (String) request.getClass().getDeclaredMethod(methodName).invoke(request);
        } catch (Exception e) {
            return "no info";
        }
    }
    public static Map<String, Object> getRequestMeta(Object request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, Object> requestMeta = new HashMap(16);
//        requestMeta.put("contextPath", this.getContextPath());
        requestMeta.put("contextPath", getRequestInfo(request,"getContextPath"));
//        requestMeta.put("servletPath", this.getServletPath());
        requestMeta.put("servletPath", getRequestInfo(request,"getServletPath"));
//        requestMeta.put("requestURL", this.getRequestURL());
        requestMeta.put("requestURL", getRequestInfo(request,"getRequestURL"));
//        requestMeta.put("requestURI", this.getRequestURI());
        requestMeta.put("requestURI", getRequestInfo(request,"getRequestURI"));
//        requestMeta.put("method", this.getMethod());
        requestMeta.put("method", getRequestInfo(request,"getMethod"));
//        requestMeta.put("serverName", this.getServerName());
//        requestMeta.put("serverPort", this.getServerPort());
//        requestMeta.put("queryString", this.getQueryString());
        requestMeta.put("queryString", getRequestInfo(request,"getQueryString"));
//        requestMeta.put("protocol", this.getProtocol());
//        requestMeta.put("scheme", this.getScheme());
//        requestMeta.put("remoteAddr", this.getDongTaiRemoteAddr());
//        requestMeta.put("secure", this.isSecure());
//        requestMeta.put("body", "");
//        requestMeta.put("headers", this.getHeaders());
//        requestMeta.put("replay-request", null != this.getHeader("dongtai-replay-id"));
        return requestMeta;
//        Method methodOfRequestMeta = request.getClass().getDeclaredMethod("getRequestMeta");
//        return (Map)methodOfRequestMeta.invoke(request);
    }
//
//    public static String getPostBody(Object request) {
//        try {
//            Method methodOfRequestMeta = request.getClass().getDeclaredMethod("getPostBody");
//            return (String)methodOfRequestMeta.invoke(request);
//        } catch (NoSuchMethodException var2) {
//            DongTaiLog.error("HttpImpl getPostBody failed", var2);
//        } catch (IllegalAccessException var3) {
//            DongTaiLog.error("HttpImpl getPostBody failed", var3);
//        } catch (InvocationTargetException var4) {
//            DongTaiLog.error("HttpImpl getPostBody failed", var4);
//        }
//
//        return null;
//    }
//
//    public static Map<String, Object> getResponseMeta(Object response) {
//        Method methodOfRequestMeta = null;
//
//        try {
//            methodOfRequestMeta = response.getClass().getDeclaredMethod("getResponseMeta", Boolean.TYPE);
////            boolean getBody = (Boolean)ConfigBuilder.getInstance().getConfig(ConfigKey.REPORT_RESPONSE_BODY).get();
//            boolean getBody = true;
//            return (Map)methodOfRequestMeta.invoke(response, getBody);
//        } catch (NoSuchMethodException var3) {
////            DongTaiLog.error("HttpImpl getResponseMeta failed", var3);
//        } catch (IllegalAccessException var4) {
////            DongTaiLog.error("HttpImpl getResponseMeta failed", var4);
//        } catch (InvocationTargetException var5) {
////            DongTaiLog.error("HttpImpl getResponseMeta failed", var5);
//        }
//
//        return null;
//    }

    public static void solveHttp(MethodEvent event) {
        System.out.println("[+]solveHttp");
        //org.apache.catalina.core.ApplicationFilterChain.doFilter(javax.servlet.ServletRequest,javax.servlet.ServletResponse)
        try {
            REQUEST_META.set(getRequestMeta(event.parameterInstances[0]));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

//        try {
//            Config<RequestDenyList> config = ConfigBuilder.getInstance().getConfig(ConfigKey.REQUEST_DENY_LIST);
//            RequestDenyList requestDenyList = (RequestDenyList)config.get();
//            if (requestDenyList != null) {
//                String requestURL = ((StringBuffer)((Map)REQUEST_META.get()).get("requestURL")).toString();
//                Map<String, String> headers = (Map)((Map)REQUEST_META.get()).get("headers");
//                if (requestDenyList.match(requestURL, headers)) {
//                    DongTaiLog.trace("HTTP Request {} deny to collect {}", new Object[]{requestURL, requestDenyList});
//                    return;
//                }
//            }
//        } catch (Throwable var5) {
//        }

//        Boolean isReplay = (Boolean)((Map)REQUEST_META.get()).get("replay-request");
//        if (isReplay) {
//            EngineManager.ENTER_REPLAY_ENTRYPOINT.enterEntry();
//        }

        //后缀/黑名单过滤
        System.out.println("requestURI:");
        System.out.println(REQUEST_META.get().get("requestURI"));// /vuldemo /favicon.ico /error
        if (!ConfigMatcher.getInstance().disableExtension((String)((Map)REQUEST_META.get()).get("requestURI"))) {
//            if (!ConfigMatcher.getInstance().getBlackUrl((Map)REQUEST_META.get())) {
                EngineManager.enterHttpEntry((Map)REQUEST_META.get());
//                DongTaiLog.debug("HTTP Request:{} {} from: {}", new Object[]{((Map)REQUEST_META.get()).get("method"), ((Map)REQUEST_META.get()).get("requestURI"), event.signature});
//            }
        }
    }

//    public static IastClassLoader getClassLoader() {
//        return iastClassLoader;
//    }
//
//    static {
//        if (!IAST_REQUEST_JAR_PACKAGE.exists()) {
//            HttpClientUtils.downloadRemoteJar("/api/v1/engine/download?engineName=dongtai-api", IAST_REQUEST_JAR_PACKAGE.getAbsolutePath());
//        }
//
//    }
}

