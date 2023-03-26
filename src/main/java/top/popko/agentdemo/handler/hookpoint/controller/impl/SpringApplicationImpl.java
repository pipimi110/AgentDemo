package top.popko.agentdemo.handler.hookpoint.controller.impl;


import java.lang.reflect.Method;

public class SpringApplicationImpl {
//    private static IastClassLoader iastClassLoader;
    public static Method getAPI;
    public static boolean isSend;

    public SpringApplicationImpl() {
    }

    public static void getWebApplicationContext(Object applicationContext) {
        //todo: spring需要添加什么http信息吗,感觉不用
        System.out.println("getWebApplicationContext");
//        if (!isSend && getClassLoader() != null) {
//            loadApplicationContext();
//            GetApiThread getApiThread = new GetApiThread(applicationContext);
//            getApiThread.start();
//        }

    }

//    private static IastClassLoader getClassLoader() {
//        iastClassLoader = HttpImpl.getClassLoader();
//        return iastClassLoader;
//    }
//
//    private static void loadApplicationContext() {
//        if (getAPI == null) {
//            try {
//                Class<?> proxyClass = iastClassLoader.loadClass("cn.huoxian.iast.spring.SpringApplicationContext");
//                getAPI = proxyClass.getDeclaredMethod("getAPI", Object.class);
//            } catch (NoSuchMethodException var4) {
////                DongTaiLog.error("SpringApplicationImpl.loadApplicationContext failed", var4);
//            } finally {
//                iastClassLoader = null;
//            }
//        }
//
//    }
}
