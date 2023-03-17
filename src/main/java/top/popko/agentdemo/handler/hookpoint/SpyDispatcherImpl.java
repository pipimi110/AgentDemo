package top.popko.agentdemo.handler.hookpoint;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;

public class SpyDispatcherImpl implements SpyDispatcher {
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

    }

    @Override
    public void leaveSink() {

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

    @Override
    public boolean collectMethodPool(Object var1, Object[] var2, Object var3, String var4, String var5, String var6, String var7, String var8, boolean var9, int var10) {
        return false;
    }

    public boolean collectMethod(Object instance, Object[] parameters, Object retObject, String policyKey, String className, String matchedClassName, String methodName, String signature, boolean isStatic) {
//        try {
////            ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
//            PolicyNode policyNode = this.getPolicyNode(policyKey);
//            if (policyNode != null) {
//                if (!this.isCollectAllowed(className, methodName, signature, policyNode.getType().getType(), false)) {
//                    return false;
//                }
//
//                MethodEvent event = new MethodEvent(className, matchedClassName, methodName, signature, instance, parameters, retObject);
//                if (policyNode instanceof SourceNode) {
//                    SourceImpl.solveSource(event, (SourceNode)policyNode, INVOKE_ID_SEQUENCER);
//                    return true;
//                }
//
////                if (policyNode instanceof PropagatorNode) {
////                    PropagatorImpl.solvePropagator(event, (PropagatorNode)policyNode, INVOKE_ID_SEQUENCER);
////                    var12 = true;
////                    return var12;
////                }
////
////                if (policyNode instanceof SinkNode) {
////                    SinkImpl.solveSink(event, (SinkNode)policyNode);
////                    var12 = true;
////                    return var12;
////                }
//
//                return false;
//            }
//        } catch (Throwable var16) {
////            DongTaiLog.error("collect method failed", var16);
//            return false;
//        } finally {
////            ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
//        }

        return false;
    }
//    private PolicyNode getPolicyNode(String policyKey) {
//        AgentEngine agentEngine = AgentEngine.getInstance();
//        PolicyManager policyManager = agentEngine.getPolicyManager();
//        if (policyManager == null) {
//            return null;
//        } else {
//            Policy policy = policyManager.getPolicy();
//            return policy == null ? null : policy.getPolicyNode(policyKey);
//        }
//    }
}
