package top.popko.agentdemo.enhance.plugin.core.adapter;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import top.popko.agentdemo.enhance.asm.AsmMethods;
import top.popko.agentdemo.enhance.asm.AsmTypes;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;

import java.lang.reflect.Modifier;
import java.util.Set;

//public class MethodAdviceAdapter extends AbstractAdviceAdapter implements AsmTypes, AsmMethods {
public class MethodAdviceAdapter extends AbstractAdviceAdapter {
//    private final Set<PolicyNode> policyNodes;
    private final MethodAdapter[] methodAdapters;
    private Label exHandler;

//    public MethodAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature, Set<PolicyNode> policyNodes, MethodContext context, MethodAdapter[] methodAdapters) {
    public MethodAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature,MethodAdapter[] methodAdapters) {
//        super(mv, access, name, descriptor, signature, context);
        super(mv, access, name, descriptor, signature);
//        this.policyNodes = policyNodes;
        this.methodAdapters = methodAdapters;
    }

    protected void before() {
    }

    protected void after(int opcode) {
    }

    protected void onMethodEnter() {
//        if (this.policyNodes != null && !this.policyNodes.isEmpty()) {
//            this.tryLabel = new Label();
//            this.visitLabel(this.tryLabel);
            this.enterMethod();
//            this.catchLabel = new Label();
//            this.exHandler = new Label();
//        }

    }

    private void enterMethod() {
        MethodAdapter[] var1 = this.methodAdapters;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            MethodAdapter methodAdapter = var1[var3];
//            methodAdapter.onMethodEnter(this, this.mv, this.context, this.policyNodes);
            methodAdapter.onMethodEnter(this, this.mv);
        }

    }

    protected void onMethodExit(int opcode) {
//        if (opcode != 191 && this.policyNodes != null && !this.policyNodes.isEmpty()) {
            this.leaveMethod(opcode);
//        }
    }

    private void leaveMethod(int opcode) {
        MethodAdapter[] var2 = this.methodAdapters;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            MethodAdapter methodAdapter = var2[var4];
//            methodAdapter.onMethodExit(this, this.mv, opcode, this.context, this.policyNodes);
            methodAdapter.onMethodExit(this, this.mv, opcode);
        }

    }

//    public void visitMaxs(int maxStack, int maxLocals) {
//        if (this.policyNodes != null && !this.policyNodes.isEmpty()) {
//            this.visitLabel(this.catchLabel);
//            this.visitLabel(this.exHandler);
//            this.leaveMethod(191);
//            this.throwException();
//            this.visitTryCatchBlock(this.tryLabel, this.catchLabel, this.exHandler, ASM_TYPE_THROWABLE.getInternalName());
//            super.visitMaxsNew(maxStack, maxLocals);
//        }
//
//    }


}
