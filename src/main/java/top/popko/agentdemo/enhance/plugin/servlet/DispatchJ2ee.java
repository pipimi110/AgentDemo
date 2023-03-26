package top.popko.agentdemo.enhance.plugin.servlet;

import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.DispatchPlugin;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

import java.lang.reflect.Modifier;
import java.util.Set;

public class DispatchJ2ee implements DispatchPlugin {
    private final String FILTER = " javax.servlet.Filter".substring(1);//interface
    private final String FILTER_CHAIN = " javax.servlet.FilterChain".substring(1);//interface
    private final String HTTP_SERVLET = " javax.servlet.http.HttpServlet".substring(1);//abstract
    private final String JAKARTA_SERVLET = " jakarta.servlet.http.HttpServlet".substring(1);
    private final String FACES_SERVLET = " javax.faces.webapp.FacesServlet".substring(1);

    public DispatchJ2ee() {
    }

    public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext context, Policy policy) {
        String className = context.getClassName();
        Set<String> ancestors = context.getAncestors();
//        if (Modifier.isInterface(context.getModifier())) {
//            DongTaiLog.trace("Ignoring interface " + className);
//        } else
        if (this.isServletDispatch(className, ancestors) || this.isJakartaServlet(className)) {
            classVisitor = new ServletClassVisitor(classVisitor, context);
        }

        return classVisitor;
    }

    private boolean isServletDispatch(String className, Set<String> diagram) {
        boolean isServlet = this.FACES_SERVLET.equals(className);
        isServlet = isServlet || this.HTTP_SERVLET.equals(className);//abstract
        return isServlet || diagram.contains(this.FILTER) || diagram.contains(this.FILTER_CHAIN);//interface
    }

    private boolean isJakartaServlet(String className) {
        return this.JAKARTA_SERVLET.equals(className);
    }
}
