package top.popko.agentdemo;

import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyManager;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.HashSet;

public class AgentDemo1 {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("static agent,,,");
        insertHook(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("dynamic agent,,,");
        insertHook(agentArgs, inst);
    }

    public static void before(String agentArgs, Instrumentation inst) {
        PolicyManager.getInstance().loadPolicy(System.getProperty("policyfile"));
        ConfigMatcher.getInstance().setInst(inst);
        EngineManager.getInstance(1);
//        EngineManager.REQUEST_CONTEXT.set(requestMeta);
//        vuldemo 非http需要初始化
        EngineManager.TRACK_MAP.set(new HashMap(1024));
        EngineManager.TAINT_HASH_CODES.set(new HashSet());

    }
    public static void after(String agentArgs, Instrumentation inst) {
        SpyDispatcherHandler.setDispatcher(new SpyDispatcherImpl());
    }
    public static void insertHook(String agentArgs, Instrumentation inst) {
        System.out.println("agentArgs : " + agentArgs);
        before(agentArgs, inst);
        DefineTransformer defineTransformer = new DefineTransformer(inst);
        inst.addTransformer(defineTransformer, true);
        defineTransformer.retransform(inst);//添加DefineTransformer后retransform重新加载类会经过DefineTransformer
        after(agentArgs, inst);
    }

}
