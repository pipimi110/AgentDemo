package top.popko.agentdemo.enhance.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

public abstract class AbstractClassVisitor extends ClassVisitor {
    protected ClassContext context;
    protected Policy policy;
    private boolean transformed;

    public AbstractClassVisitor(ClassVisitor classVisitor, ClassContext context) {
        super(Opcodes.ASM5, classVisitor);
        this.context = context;
        this.transformed = false;
    }

    public AbstractClassVisitor(ClassVisitor classVisitor, ClassContext context, Policy policy) {
        super(Opcodes.ASM5, classVisitor);
        this.context = context;
        this.policy = policy;
        this.transformed = false;
    }

    public void setTransformed() {
        this.transformed = true;//visitMethod()匹配到才设置
    }

    public boolean hasTransformed() {
        return this.transformed;
    }
}
