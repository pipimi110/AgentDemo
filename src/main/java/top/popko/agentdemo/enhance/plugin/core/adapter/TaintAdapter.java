package top.popko.agentdemo.enhance.plugin.core.adapter;

import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.asm.AsmMethods;
import top.popko.agentdemo.enhance.asm.AsmTypes;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;

import java.util.Set;

public abstract class TaintAdapter implements AsmTypes, AsmMethods {
    public TaintAdapter() {
    }

//    public abstract void onMethodEnter(MethodAdviceAdapter var1, MethodVisitor var2, MethodContext var3, Set<PolicyNode> var4);
    public abstract void onMethodEnter(MethodAdviceAdapter var1, MethodVisitor var2, Set<PolicyNode> var4);

//    public abstract void onMethodExit(MethodAdviceAdapter var1, MethodVisitor var2, int var3, MethodContext var4, Set<PolicyNode> var5);
    public abstract void onMethodExit(MethodAdviceAdapter var1, MethodVisitor var2, int var3, Set<PolicyNode> var5);
}
