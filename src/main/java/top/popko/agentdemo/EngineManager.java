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
//    private final boolean saveBytecode;
    public static final RequestContext REQUEST_CONTEXT = new RequestContext();
//    public static final IastTrackMap TRACK_MAP = new IastTrackMap();
//    public static final IastTaintHashCodes TAINT_HASH_CODES = new IastTaintHashCodes();
//    public static final TaintRangesPool TAINT_RANGES_POOL = new TaintRangesPool();
//    public static IastServer SERVER;
//    public static final AgentState AGENT_STATE = AgentState.getInstance();
//    private static final AtomicInteger reqCounts = new AtomicInteger(0);
//    public static final BooleanThreadLocal ENTER_REPLAY_ENTRYPOINT = new BooleanThreadLocal(false);
//
    private EngineManager(int agentId) {
//        PropertyUtils cfg = PropertyUtils.getInstance();
//        this.saveBytecode = cfg.isEnableDumpClass();
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
//        TAINT_RANGES_POOL.remove();
//        ENTER_REPLAY_ENTRYPOINT.remove();
//        ContextManager.getCONTEXT().remove();
//        ScopeManager.SCOPE_TRACKER.remove();
    }
//
//    public static void maintainRequestCount() {
//        reqCounts.getAndIncrement();
//    }
//
//    public static int getRequestCount() {
//        return reqCounts.get();
//    }

//    public static boolean isEngineRunning() {
//        return AGENT_STATE.isRunning();
//    }
//
//    public boolean isEnableDumpClass() {
//        return this.saveBytecode;
//    }
//
//    public static Integer getAgentId() {
//        return instance.agentId;
//    }

    public static void enterHttpEntry(Map<String, Object> requestMeta) {
//        ServiceFactory.startService();
//        String protocol;
//        if (null == SERVER) {
//            String url = null;
//            protocol = null;
//            if (null != requestMeta.get("serverName")) {
//                url = (String)requestMeta.get("serverName");
//            } else {
//                url = "";
//            }
//
//            if (null != requestMeta.get("protocol")) {
//                protocol = (String)requestMeta.get("protocol");
//            } else {
//                protocol = "";
//            }
//
//            SERVER = new IastServer(url, (Integer)requestMeta.get("serverPort"), protocol, true);
//            ServerAddressReport serverAddressReport = new ServerAddressReport(SERVER.getServerAddr(), SERVER.getServerPort(), SERVER.getProtocol());
//            serverAddressReport.run();
//        }
//
//        Map<String, String> headers = (Map)requestMeta.get("headers");
//        if (headers.containsKey("dt-traceid")) {
//            ContextManager.getOrCreateGlobalTraceId((String)headers.get("dt-traceid"), getAgentId());
//        } else {
//            protocol = ContextManager.getOrCreateGlobalTraceId((String)null, getAgentId());
//            String spanId = ContextManager.getSpanId(protocol, getAgentId());
//            headers.put("dt-traceid", protocol);
//            headers.put("dt-spandid", spanId);
//        }

        //线程资源初始化//todo: 限制重复执行//每个filter都会执行,,用黑名单
        REQUEST_CONTEXT.set(requestMeta);//todo:防止remove失败?防止处理非http?不初始化thread为空,不能操作
        TRACK_MAP.set(new HashMap(1024));
        TAINT_HASH_CODES.set(new HashSet());
//        TAINT_RANGES_POOL.set(new HashMap());
//        ScopeManager.SCOPE_TRACKER.getHttpEntryScope().enter();
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
//        after(agentArgs, inst);a
        DefineTransformer defineTransformer = new DefineTransformer(inst);
        inst.addTransformer(defineTransformer, true);
        defineTransformer.retransform(inst);//添加DefineTransformer后retransform重新加载类会经过DefineTransformer
        after(agentArgs, inst);
    }
}
