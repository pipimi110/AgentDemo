package top.popko.agentdemo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineManager {
//    private static EngineManager instance;
//    private final int agentId;
//    private final boolean saveBytecode;
//    public static final RequestContext REQUEST_CONTEXT = new RequestContext();
//    public static final IastTrackMap TRACK_MAP = new IastTrackMap();
//    public static final IastTaintHashCodes TAINT_HASH_CODES = new IastTaintHashCodes();
//    public static final TaintRangesPool TAINT_RANGES_POOL = new TaintRangesPool();
//    public static IastServer SERVER;
//    public static final AgentState AGENT_STATE = AgentState.getInstance();
//    private static final AtomicInteger reqCounts = new AtomicInteger(0);
//    public static final BooleanThreadLocal ENTER_REPLAY_ENTRYPOINT = new BooleanThreadLocal(false);
//
//    public static EngineManager getInstance() {
//        return instance;
//    }
//
//    public static EngineManager getInstance(int agentId) {
//        if (instance == null) {
//            instance = new EngineManager(agentId);
//        }
//
//        return instance;
//    }
//
//    private EngineManager(int agentId) {
//        PropertyUtils cfg = PropertyUtils.getInstance();
//        this.saveBytecode = cfg.isEnableDumpClass();
//        this.agentId = agentId;
//    }
//
//    public static void cleanThreadState() {
//        REQUEST_CONTEXT.remove();
//        TRACK_MAP.remove();
//        TAINT_HASH_CODES.remove();
//        TAINT_RANGES_POOL.remove();
//        ENTER_REPLAY_ENTRYPOINT.remove();
//        ContextManager.getCONTEXT().remove();
//        ScopeManager.SCOPE_TRACKER.remove();
//    }
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

        //线程资源初始化
//        REQUEST_CONTEXT.set(requestMeta);
//        TRACK_MAP.set(new HashMap(1024));
//        TAINT_HASH_CODES.set(new HashSet());
//        TAINT_RANGES_POOL.set(new HashMap());
//        ScopeManager.SCOPE_TRACKER.getHttpEntryScope().enter();
    }
}
