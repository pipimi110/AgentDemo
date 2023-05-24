package top.popko.agentdemo.enhance.plugin.servlet;

import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.DispatchPlugin;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

import java.lang.reflect.Modifier;
import java.util.Set;

public class DispatchJ2ee implements DispatchPlugin {
    private final String HTTP_SERVLET = " javax.servlet.http.HttpServlet".substring(1);//abstract
    private final String JAKARTA_SERVLET = " jakarta.servlet.http.HttpServlet".substring(1);

    public DispatchJ2ee() {
    }

    public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext context, Policy policy) {
        String className = context.getClassName();
        Set<String> ancestors = context.getAncestors();
        if (this.isServletDispatch(className, ancestors) || this.isJakartaServlet(className)) {
            classVisitor = new ServletClassVisitor(classVisitor, context);
        }

        return classVisitor;
    }

    private boolean isServletDispatch(String className, Set<String> diagram) {
        return this.HTTP_SERVLET.equals(className);
    }

    private boolean isJakartaServlet(String className) {
        return this.JAKARTA_SERVLET.equals(className);
    }
}
