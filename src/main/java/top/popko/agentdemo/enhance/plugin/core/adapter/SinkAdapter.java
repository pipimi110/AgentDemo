package top.popko.agentdemo.enhance.plugin.core.adapter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.SinkNode;

import java.util.Iterator;
import java.util.Set;

public class SinkAdapter extends TaintAdapter {
    public SinkAdapter() {
    }

//    public void onMethodEnter(MethodAdviceAdapter adapter, MethodVisitor mv, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodEnter(AbstractAdviceAdapter adapter, MethodVisitor mv, Set<PolicyNode> policyNodes) {
        Iterator var5 = policyNodes.iterator();

        while(var5.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var5.next();
            if (policyNode instanceof SinkNode) {
                this.enterScope(adapter);
                adapter.trackMethod(-1, policyNode, false);
            }
        }

    }

//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodExit(AbstractAdviceAdapter adapter, MethodVisitor mv, int opcode, Set<PolicyNode> policyNodes) {
        Iterator var6 = policyNodes.iterator();

        while(var6.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var6.next();
            if (policyNode instanceof SinkNode) {
                this.leaveScope(adapter);
            }
        }

    }

    private void enterScope(AbstractAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterSink);
    }

    private void leaveScope(AbstractAdviceAdapter adapter) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leaveSink);
    }
}
