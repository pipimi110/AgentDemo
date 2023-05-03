package top.popko.agentdemo.handler.hookpoint;

import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.handler.hookpoint.controller.HookType;
import top.popko.agentdemo.handler.hookpoint.controller.impl.*;
import top.popko.agentdemo.handler.hookpoint.controller.scope.ScopeManager;
import top.popko.agentdemo.handler.hookpoint.graph.GraphBuilder;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.*;

import java.util.concurrent.atomic.AtomicInteger;

public class SpyDispatcherImpl implements SpyDispatcher {
    public static final AtomicInteger INVOKE_ID_SEQUENCER = new AtomicInteger(1);

    public void methodNamePrint() {
        System.out.println("[+]".concat(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void enterHttp() {
        this.methodNamePrint();
        ScopeManager.SCOPE_TRACKER.getHttpRequestScope().enter();
    }

    @Override
    public void leaveHttp(Object request, Object response) {
        this.methodNamePrint();
        ScopeManager.SCOPE_TRACKER.getHttpRequestScope().leave();
//        if (EngineManager.isEngineRunning()) {
//            try {
//                ScopeManager.SCOPE_TRACKER.getHttpRequestScope().leave();
//                if (!ScopeManager.SCOPE_TRACKER.getHttpRequestScope().in() && ScopeManager.SCOPE_TRACKER.getHttpEntryScope().in()) {
        if (!ScopeManager.SCOPE_TRACKER.getHttpRequestScope().in()) {
//                    EngineManager.maintainRequestCount();
            GraphBuilder.buildAndReport(request, response);
            EngineManager.cleanThreadState();//全部退出后输出json并清理
        }
//            } catch (Throwable var4) {
//                DongTaiLog.error("leave http failed", var4);
//                EngineManager.cleanThreadState();
//            }
//
//        }
    }

    @Override
    public boolean isFirstLevelHttp() {
        return false;
    }

    public Object cloneRequest(Object req, boolean isJakarta) {
//        if (!EngineManager.isEngineRunning()) {
//            return req;
//        } else {
        try {
            return HttpImpl.cloneRequest(req, isJakarta);
        } catch (Throwable var4) {
            return req;
        }
//        }
    }

    public Object cloneResponse(Object response, boolean isJakarta) {
//        if (!EngineManager.isEngineRunning()) {
//            return response;
//        } else {
        try {
            return HttpImpl.cloneResponse(response, isJakarta);
        } catch (Throwable var4) {
            return response;
        }
//        }
    }

    @Override
    public void enterSource() {
        this.methodNamePrint();
    }

    @Override
    public void leaveSource() {
        this.methodNamePrint();
    }

    @Override
    public boolean isFirstLevelSource() {
//        return false;
        return true;
    }

    @Override
    public void enterPropagator(boolean var1) {
        this.methodNamePrint();
    }

    @Override
    public void leavePropagator(boolean var1) {
        this.methodNamePrint();
    }

    @Override
    public boolean isFirstLevelPropagator() {
        return false;
    }

    @Override
    public void enterSink() {
        this.methodNamePrint();
    }

    @Override
    public void leaveSink() {
        this.methodNamePrint();
    }

    @Override
    public boolean isFirstLevelSink() {
        return false;
    }

    @Override
    public void reportService(String var1, String var2, String var3, String var4, String var5) {

    }

    @Override
    public boolean isReplayRequest() {
        return false;
    }

    @Override
    public boolean isNotReplayRequest() {
        return false;
    }

    public boolean collectMethodPool(Object instance, Object[] argumentArray, Object retValue, String framework, String className, String matchClassName, String methodName, String methodSign, boolean isStatic, int hookType) {
        if (ScopeManager.SCOPE_TRACKER.getHttpRequestScope().isFirst()) {//限制一次http后匹配污点规则//todo: 放在字节码操作可以减少一些处理,但是if-else报错解决不了

///*     */     try {
///* 312 */       ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
            if (!isCollectAllowed(className, methodName, methodSign, hookType, true)) {
                return false;
            }
            if (HookType.SPRINGAPPLICATION.equals(hookType)) {
                SpringApplicationImpl.getWebApplicationContext(retValue);
            } else {
                MethodEvent event = new MethodEvent(className, matchClassName, methodName, methodSign, instance, argumentArray, retValue);
                if (HookType.HTTP.equals(hookType)) {
                    HttpImpl.solveHttp(event);
                }
            }
///* 327 */     } catch (Throwable e) {
///* 328 */       DongTaiLog.error("collect method pool failed: " + e.toString(), e);
///*     */     } finally {
///* 330 */       ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
///*     */     }
        }
        return false;//todo:没做条件校验,无影响
    }

    public boolean
    collectMethod(Object instance, Object[] parameters, Object retObject, String policyKey, String className, String matchedClassName, String methodName, String signature, boolean isStatic) {
        boolean status = false;
        if (ScopeManager.SCOPE_TRACKER.getHttpRequestScope().in()) {//污点分析时http不为0
            System.out.println("[+]collectMethod: "+className);
            try {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
                PolicyNode policyNode = this.getPolicyNode(policyKey);
                if (policyNode != null && this.isCollectAllowed(className, methodName, signature, policyNode.getType().getType(), false)) {
                    MethodEvent event = new MethodEvent(className, matchedClassName, methodName, signature, instance, parameters, retObject);
                    if (policyNode instanceof SourceNode) {
                        new SourceImpl().solveSource(event, (SourceNode) policyNode);
                        status = true;
                    }

                    if (policyNode instanceof PropagatorNode) {
                        new PropagatorImpl().solvePropagator(event, (PropagatorNode) policyNode, INVOKE_ID_SEQUENCER);
                        status = true;
                    }

                    if (policyNode instanceof SinkNode) {
                        new SinkImpl().solveSink(event, (SinkNode) policyNode);
                        status = true;
                    }
                }
            } catch (Throwable var16) {
//            DongTaiLog.error("collect method failed", var16);
            } finally {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
            }
        }
        return status;
    }

    private boolean isCollectAllowed(String className, String methodName, String signature, int policyNodeType, boolean isEnterEntry) {
//        if (!isEnterEntry) {
////            if (!ScopeManager.SCOPE_TRACKER.inEnterEntry()) {
////                return false;
////            }
////
////            if (ScopeManager.SCOPE_TRACKER.getPolicyScope().isOverCapacity()) {
////                return false;
////            }
//线程存储过大返回false
//            try {
//                int methodPoolMaxSize = (Integer)ConfigBuilder.getInstance().getConfig(ConfigKey.REPORT_MAX_METHOD_POOL_SIZE).get();
//                if (methodPoolMaxSize > 0 && ((Map)EngineManager.TRACK_MAP.get()).size() >= methodPoolMaxSize) {
////                    ScopeManager.SCOPE_TRACKER.getPolicyScope().setOverCapacity(true);
////                    DongTaiLog.warn("current request method pool size over capacity: {}", new Object[]{methodPoolMaxSize});
//                    return false;
//                }
//            } catch (Throwable var7) {
//            }
//        }

        return true;
    }

    private PolicyNode getPolicyNode(String policyKey) {
//        AgentEngine agentEngine = AgentEngine.getInstance();
//        PolicyManager policyManager = agentEngine.getPolicyManager();
        PolicyManager policyManager = PolicyManager.getInstance();
        Policy policy = policyManager.getPolicy();
        return policy == null ? null : policy.getPolicyNode(policyKey);
    }
}
