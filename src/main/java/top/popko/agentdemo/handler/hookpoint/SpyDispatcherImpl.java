package top.popko.agentdemo.handler.hookpoint;

import top.popko.agentdemo.handler.hookpoint.controller.HookType;
import top.popko.agentdemo.handler.hookpoint.controller.impl.HttpImpl;
import top.popko.agentdemo.handler.hookpoint.controller.impl.SinkImpl;
import top.popko.agentdemo.handler.hookpoint.controller.impl.SourceImpl;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.*;

import java.util.concurrent.atomic.AtomicInteger;

public class SpyDispatcherImpl implements SpyDispatcher {
    public static final AtomicInteger INVOKE_ID_SEQUENCER = new AtomicInteger(1);

    @Override
    public void enterHttp() {

    }

    @Override
    public void leaveHttp(Object var1, Object var2) {

    }

    @Override
    public boolean isFirstLevelHttp() {
        return false;
    }

    @Override
    public Object cloneRequest(Object var1, boolean var2) {
        return null;
    }

    @Override
    public Object cloneResponse(Object var1, boolean var2) {
        return null;
    }

    @Override
    public void enterSource() {
        System.out.println("enterSource");
    }

    @Override
    public void leaveSource() {
        System.out.println("leaveSource");

    }

    @Override
    public boolean isFirstLevelSource() {
        return false;
    }

    @Override
    public void enterPropagator(boolean var1) {

    }

    @Override
    public void leavePropagator(boolean var1) {

    }

    @Override
    public boolean isFirstLevelPropagator() {
        return false;
    }

    @Override
    public void enterSink() {
        System.out.println("enterSink");
    }

    @Override
    public void leaveSink() {
        System.out.println("leaveSink");

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
///*     */     try {
///* 312 */       ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
/*     */
/* 314 */       if (!isCollectAllowed(className, methodName, methodSign, hookType, true)) {
/* 315 */         return false;
/*     */       }
/*     */
/* 318 */       if (HookType.SPRINGAPPLICATION.equals(hookType)) {
///* 319 */         SpringApplicationImpl.getWebApplicationContext(retValue);
/*     */       } else {
/* 321 */         MethodEvent event = new MethodEvent(className, matchClassName, methodName, methodSign, instance, argumentArray, retValue);
/*     */
/* 323 */         if (HookType.HTTP.equals(hookType)) {
/* 324 */           HttpImpl.solveHttp(event);
/*     */         }
/*     */       }
///* 327 */     } catch (Throwable e) {
///* 328 */       DongTaiLog.error("collect method pool failed: " + e.toString(), e);
///*     */     } finally {
///* 330 */       ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
///*     */     }
/* 332 */     return false;//todo:没做条件校验,无影响
/*     */   }

    public boolean collectMethod(Object instance, Object[] parameters, Object retObject, String policyKey, String className, String matchedClassName, String methodName, String signature, boolean isStatic) {
        try {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
            PolicyNode policyNode = this.getPolicyNode(policyKey);
            if (policyNode != null) {
                if (!this.isCollectAllowed(className, methodName, signature, policyNode.getType().getType(), false)) {
                    return false;
                }

                MethodEvent event = new MethodEvent(className, matchedClassName, methodName, signature, instance, parameters, retObject);
                if (policyNode instanceof SourceNode) {
                    SourceImpl.solveSource(event, (SourceNode) policyNode);
                    return true;
                }

//                if (policyNode instanceof PropagatorNode) {
//                    PropagatorImpl.solvePropagator(event, (PropagatorNode)policyNode, INVOKE_ID_SEQUENCER);
//                    var12 = true;
//                    return var12;
//                }
//
                if (policyNode instanceof SinkNode) {
                    SinkImpl.solveSink(event, (SinkNode)policyNode);
                    return true;
                }

                return false;
            }
        } catch (Throwable var16) {
//            DongTaiLog.error("collect method failed", var16);
            return false;
        } finally {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
        }

        return false;
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
