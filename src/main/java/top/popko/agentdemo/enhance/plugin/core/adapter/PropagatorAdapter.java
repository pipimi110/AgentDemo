package top.popko.agentdemo.enhance.plugin.core.adapter;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.PropagatorNode;

import java.util.Iterator;
import java.util.Set;

public class PropagatorAdapter extends TaintAdapter {
    public PropagatorAdapter() {
    }

    public void onMethodEnter(AbstractAdviceAdapter adapter, MethodVisitor mv, Set<PolicyNode> policyNodes) {
        Iterator var5 = policyNodes.iterator();

        while(var5.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var5.next();
            if (policyNode instanceof PropagatorNode) {
//                String signature = context.toString();
                String signature = "my signature";
                this.enterScope(adapter, signature);
            }
        }

    }

//    public void onMethodExit(MethodAdviceAdapter adapter, MethodVisitor mv, int opcode, MethodContext context, Set<PolicyNode> policyNodes) {
    public void onMethodExit(AbstractAdviceAdapter adapter, MethodVisitor mv, int opcode, Set<PolicyNode> policyNodes) {
        Iterator var6 = policyNodes.iterator();

        while(var6.hasNext()) {
            PolicyNode policyNode = (PolicyNode)var6.next();
            if (policyNode instanceof PropagatorNode) {
                String signature = "my signature";
                adapter.trackMethod(opcode, policyNode, true);
                this.leaveScope(adapter, signature);
            }
        }

    }

    private void enterScope(AbstractAdviceAdapter adapter, String signature) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.push(false);

        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterPropagator);
    }

    private void leaveScope(AbstractAdviceAdapter adapter, String signature) {
        adapter.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        adapter.push(false);

        adapter.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leavePropagator);
    }

}
