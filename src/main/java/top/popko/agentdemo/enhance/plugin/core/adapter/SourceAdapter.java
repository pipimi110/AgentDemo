package top.popko.agentdemo.enhance.plugin.core.adapter;


import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.SourceNode;

import java.util.Iterator;
import java.util.Set;

public class SourceAdapter extends TaintAdapter {
    public SourceAdapter() {
    }

//    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv, Set<PolicyNode> policyNodes) {
//    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv) {
        Iterator var5 = policyNodes.iterator();

        while(var5.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var5.next();
            if (policyNode instanceof SourceNode) {
                this.enterScope(adapter);
            }
        }

    }

//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, Set<PolicyNode> policyNodes) {
//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode) {
        Iterator var6 = policyNodes.iterator();

        while(var6.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var6.next();
            if (policyNode instanceof SourceNode) {
//                Label elseLabel = new Label();
//                Label endLabel = new Label();
//                this.isFirstScope(adapter);
//                mv.visitJumpInsn(153, elseLabel);
                adapter.trackMethod(opcode, policyNode, true);
//                adapter.trackMethod(opcode, true);
//                adapter.mark(elseLabel);
//                adapter.mark(endLabel);
                this.leaveScope(adapter);
            }
        }
    }

    private void enterScope(MethodAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterSource);
    }

    private void leaveScope(MethodAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leaveSource);
    }

    private void isFirstScope(MethodAdviceAdapter adapter) {
//        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$isFirstLevelSource);
    }
}
