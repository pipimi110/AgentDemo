package top.popko.agentdemo.enhance.plugin.core.adapter;

import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.asm.AsmMethods;
import top.popko.agentdemo.enhance.asm.AsmTypes;

import java.util.Set;

public abstract class MethodAdapter implements AsmTypes, AsmMethods {
    public MethodAdapter() {
    }

//    public abstract void onMethodEnter(MethodAdviceAdapter var1, MethodVisitor var2, MethodContext var3, Set<PolicyNode> var4);
    public abstract void onMethodEnter(MethodAdviceAdapter var1, MethodVisitor var2);

//    public abstract void onMethodExit(MethodAdviceAdapter var1, MethodVisitor var2, int var3, MethodContext var4, Set<PolicyNode> var5);
    public abstract void onMethodExit(MethodAdviceAdapter var1, MethodVisitor var2, int var3);
}