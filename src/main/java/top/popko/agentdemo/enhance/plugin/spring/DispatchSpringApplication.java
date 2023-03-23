package top.popko.agentdemo.enhance.plugin.spring;


import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.DispatchPlugin;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

public class DispatchSpringApplication implements DispatchPlugin {
//    private static final String FRAMEWORK_SERVLET = " org.springframework.web.servlet.FrameworkServlet".substring(1);
    private static final String FRAMEWORK_SERVLET = "org.springframework.web.servlet.FrameworkServlet";

    public DispatchSpringApplication() {
    }

    public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext context, Policy policy) {
        String className = context.getClassName();
        if (FRAMEWORK_SERVLET.equals(className)) {
            classVisitor = new SpringClassVisitor(classVisitor, context);
        }

        return classVisitor;
    }
}
