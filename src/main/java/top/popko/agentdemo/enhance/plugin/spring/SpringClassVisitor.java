package top.popko.agentdemo.enhance.plugin.spring;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.AbstractClassVisitor;

public class SpringClassVisitor extends AbstractClassVisitor {
    public SpringClassVisitor(ClassVisitor classVisitor, ClassContext context) {
        super(classVisitor, context);
    }

    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ("getWebApplicationContext".equals(name)) {
            methodVisitor = new SpringApplicationAdviceAdapter(methodVisitor, access, name, descriptor, this.context, "SPRINGAPPLICATION_FOR_API", "SPRINGAPPLICATION_FOR_API");
            this.setTransformed();
        }

        return methodVisitor;
    }
}
