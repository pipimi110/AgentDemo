//package top.popko.agentdemo.enhance.plugin.core.adapter;
//
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
//
//import java.util.Iterator;
//import java.util.Set;
//
//public class PropagatorAdapter extends MethodAdapter {
//    public PropagatorAdapter() {
//    }
//
//    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv, Set<PolicyNode> policyNodes) {
//        Iterator var5 = policyNodes.iterator();
//
//        while(var5.hasNext()) {
//            PolicyNode policyNode = (PolicyNode)var5.next();
//            if (policyNode instanceof PropagatorNode) {
//                String signature = context.toString();
//                this.enterScope(adapter, signature);
//            }
//        }
//
//    }
//
//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, MethodContext context, Set<PolicyNode> policyNodes) {
//        Iterator var6 = policyNodes.iterator();
//
//        while(var6.hasNext()) {
//            PolicyNode policyNode = (PolicyNode)var6.next();
//            if (policyNode instanceof PropagatorNode) {
//                Label elseLabel = new Label();
//                Label endLabel = new Label();
//                String signature = context.toString();
//                this.isFirstScope(adapter);
//                mv.visitJumpInsn(153, elseLabel);
//                adapter.trackMethod(opcode, policyNode, true);
//                adapter.mark(elseLabel);
//                adapter.mark(endLabel);
//                this.leaveScope(adapter, signature);
//            }
//        }
//
//    }
//
//    private void enterScope(MethodAdviceAdapter adapter, String signature) {
//        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
//        if (PropagatorImpl.isSkipScope(signature)) {
//            adapter.push(true);
//        } else {
//            adapter.push(false);
//        }
//
//        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterPropagator);
//    }
//
//    private void leaveScope(MethodAdviceAdapter adapter, String signature) {
//        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
//        if (PropagatorImpl.isSkipScope(signature)) {
//            adapter.push(true);
//        } else {
//            adapter.push(false);
//        }
//
//        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leavePropagator);
//    }
//
//    private void isFirstScope(MethodAdviceAdapter adapter) {
//        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
//        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$isFirstLevelPropagator);
//    }
//}
