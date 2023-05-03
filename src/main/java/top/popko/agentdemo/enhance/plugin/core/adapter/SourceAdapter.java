package top.popko.agentdemo.enhance.plugin.core.adapter;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.SourceNode;

import java.util.Iterator;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class SourceAdapter extends TaintAdapter {
    public SourceAdapter() {
    }

    //    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodEnter(AbstractAdviceAdapter adapter, MethodVisitor mv, Set<PolicyNode> policyNodes) {
//    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv) {
        Iterator var5 = policyNodes.iterator();

        while (var5.hasNext()) {
            PolicyNode policyNode = (PolicyNode) var5.next();
            if (policyNode instanceof SourceNode) {
                this.enterScope(adapter);
            }
        }

    }

    //    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodExit(AbstractAdviceAdapter adapter, MethodVisitor mv, int opcode, Set<PolicyNode> policyNodes) {
//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode) {
        Iterator var6 = policyNodes.iterator();

        while (var6.hasNext()) {
            PolicyNode policyNode = (PolicyNode) var6.next();
            if (policyNode instanceof SourceNode) {
//                Label elseLabel = new Label();
//                this.isFirstScope(adapter);
////                mv.visitJumpInsn(153, elseLabel);
//                // 将返回值从操作数栈中弹出，并将其存储到本地变量中
//                int iftestReturn = adapter.newLocal(Type.BOOLEAN_TYPE);
//                adapter.visitVarInsn(ISTORE, iftestReturn);
//// 使用 IFNE 指令校验返回值是否为 true
//                adapter.visitVarInsn(ILOAD, iftestReturn);
////                mv.visitJumpInsn(IFEQ, elseLabel);//iftestReturn!=0跳转到label
                // 如果返回值为 true，执行相应的操作
                adapter.trackMethod(opcode, policyNode, true);
                // 标签：如果返回值为 false，则跳过上一步的操作，继续执行后面的指令
//                adapter.mark(elseLabel);
                this.leaveScope(adapter);
            }
        }
    }

    private void enterScope(AbstractAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterSource);
    }

    private void leaveScope(AbstractAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leaveSource);
    }

    private void isFirstScope(AbstractAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$isFirstLevelSource);
    }
}
