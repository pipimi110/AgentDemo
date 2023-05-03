package top.popko.agentdemo;


import top.popko.agentdemo.util.IASTClassLoader;

import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AgentDemo1 {
    private static IASTClassLoader IAST_CLASS_LOADER;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("static agent,,,");

        insertHook(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("dynamic agent,,,");
        insertHook(agentArgs, inst);
    }

    public static void insertHook(String agentArgs, Instrumentation inst) {
        EngineManager.insertHook(agentArgs, inst);
//        try {
//            if (IAST_CLASS_LOADER == null) {
//                IAST_CLASS_LOADER = new IASTClassLoader("D:\\javaWebTool\\AgentDemo_myiast\\target\\AgentDemo-1.1-SNAPSHOT-jar-with-dependencies.jar");
//            }
//            Class<?> classOfEngine = IAST_CLASS_LOADER.loadClass("top.popko.agentdemo.EngineManager");
//            classOfEngine.getMethod("insertHook", String.class, Instrumentation.class).invoke(null,agentArgs, inst);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}