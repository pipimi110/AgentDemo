package top.popko.agentdemo.enhance.plugin.core.adapter;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.MethodContext;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;

import java.util.Set;

//public class ConstructorAdviceAdapter extends AbstractAdviceAdapter implements AsmTypes, AsmMethods {
public class ConstructorAdviceAdapter extends AbstractAdviceAdapter {
    private final Set<PolicyNode> policyNodes;
    private final TaintAdapter[] methodAdapters;
    private Label exHandler;

    public ConstructorAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature, Set<PolicyNode> policyNodes, MethodContext context, TaintAdapter[] methodAdapters) {
//    public ConstructorAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature,MethodAdapter[] methodAdapters) {
        super(mv, access, name, descriptor, signature, context);
//        super(mv, access, name, descriptor, signature);
        this.policyNodes = policyNodes;
        this.methodAdapters = methodAdapters;
    }

    public void enterConstructor() {
        TaintAdapter[] var1 = this.methodAdapters;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TaintAdapter methodAdapter = var1[var3];
//            methodAdapter.onMethodEnter(this, this.mv, this.context, this.policyNodes);
            methodAdapter.onMethodEnter(this, this.mv, this.policyNodes);
        }

    }
    protected void onMethodExit(int opcode) {
        if (opcode != 191 && this.policyNodes != null && !this.policyNodes.isEmpty()) {
            this.leaveConstructor(opcode);
        }
    }
    public void leaveConstructor(int opcode) {
        TaintAdapter[] var2 = this.methodAdapters;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            TaintAdapter methodAdapter = var2[var4];
//            methodAdapter.onMethodExit(this, this.mv, opcode, this.context, this.policyNodes);
            methodAdapter.onMethodExit(this, this.mv, opcode, this.policyNodes);
        }

    }
    @Override
    public void visitCode() {
        super.visitCode();
        // 在方法开始处插入要执行的代码
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("[]Code executed before the method body");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        enterConstructor();//在方法执行时插入代码并优先执行，可以使用 AdviceAdapter 的 visitCode() 方法，在方法开始执行时插入代码
    }
    @Override
    protected void before() {

    }

    @Override
    protected void after(int var1) {

    }
}
