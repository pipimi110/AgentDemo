package top.popko.agentdemo;



import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class AgentDemo1 {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("static agent,,,");

        insertHook(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("dynamic agent,,,");
        insertHook(agentArgs, inst);
    }

    public static void insertHook(String agentArgs, Instrumentation inst) {
        try {
            JarFileHelper.addJarToBootstrap(inst);
            EngineManager.insertHook(agentArgs, inst);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}