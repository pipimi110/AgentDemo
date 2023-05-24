package top.popko.agentdemo;

import top.popko.agentdemo.handler.hookpoint.models.track.RequestContext;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintHashCodes;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintMap;
import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.handler.hookpoint.NopSpy;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyManager;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EngineManager {
    public static final TaintHashCodes TAINT_HASH_CODES = new TaintHashCodes();
    public static final TaintMap TRACK_MAP = new TaintMap();

    private static EngineManager instance;
    private final int agentId;
    public static final RequestContext REQUEST_CONTEXT = new RequestContext();
    private EngineManager(int agentId) {
        this.agentId = agentId;
    }
    public static EngineManager getInstance() {
        return instance;
    }

    public static EngineManager getInstance(int agentId) {
        if (instance == null) {
            instance = new EngineManager(agentId);
        }

        return instance;
    }
    public static void cleanThreadState() {
        REQUEST_CONTEXT.remove();
        TRACK_MAP.remove();
        TAINT_HASH_CODES.remove();
    }

    public static void enterHttpEntry(Map<String, Object> requestMeta) {

        //线程资源初始化//todo: 限制重复执行//每个filter都会执行,,用黑名单
        REQUEST_CONTEXT.set(requestMeta);//todo:防止remove失败?防止处理非http?不初始化thread为空,不能操作
        TRACK_MAP.set(new HashMap(1024));
        TAINT_HASH_CODES.set(new HashSet());
    }

    public static void before(String agentArgs, Instrumentation inst) {
        PolicyManager.getInstance().loadPolicy(System.getProperty("configpath"));
        ConfigMatcher.getInstance().setInst(inst);
        EngineManager.getInstance(1);
//        EngineManager.REQUEST_CONTEXT.set(requestMeta);
//        vuldemo 非http需要初始化
        EngineManager.TRACK_MAP.set(new HashMap(1024));
        EngineManager.TAINT_HASH_CODES.set(new HashSet());
        SpyDispatcherHandler.setDispatcher(new NopSpy());

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
