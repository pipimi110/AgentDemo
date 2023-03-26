package top.popko.agentdemo.enhance.plugin.servlet;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.AbstractClassVisitor;
import top.popko.agentdemo.util.AsmUtils;

public class ServletClassVisitor extends AbstractClassVisitor {
    private final String HTTP_SERVLET_REQUEST = " javax.servlet.http.HttpServletRequest".substring(1);
    private final String HTTP_SERVLET_RESPONSE = " javax.servlet.http.HttpServletResponse".substring(1);
    private final String SERVLET_REQUEST = " javax.servlet.ServletRequest".substring(1);
    private final String SERVLET_RESPONSE = " javax.servlet.ServletResponse".substring(1);
    private final String FILTER_CHAIN = " javax.servlet.FilterChain".substring(1);
    private final String JAKARTA_SERVLET_REQUEST_HTTP = " jakarta.servlet.http.HttpServletRequest".substring(1);
    private final String JAKARTA_SERVLET_REQUEST = " jakarta.servlet.ServletRequest".substring(1);
    private final String JAKARTA_SERVLET_RESPONSE_HTTP = " jakarta.servlet.http.HttpServletResponse".substring(1);
    private final String JAKARTA_SERVLET_RESPONSE = " jakarta.servlet.ServletResponse".substring(1);
    private boolean isFaces;
    private boolean isJakarta;

    ServletClassVisitor(ClassVisitor classVisitor, ClassContext context) {
        super(classVisitor, context);
        this.isFaces = " javax.faces.webapp.FacesServlet".substring(1).equals(context.getClassName());
        this.isJakarta = " jakarta.servlet.http.HttpServlet".substring(1).equals(context.getClassName());
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        Type[] typeOfArgs = Type.getArgumentTypes(desc);
        String signCode = AsmUtils.buildSignature(this.context.getClassName(), name, desc);
        if (this.isService(name, typeOfArgs) || this.isJakarta && this.isJakartaArgs(typeOfArgs) || this.isFaces && this.isFacesArgs(typeOfArgs)) {
//            DongTaiLog.debug("Adding HTTP tracking for type {}", new Object[]{this.context.getClassName()});
            mv = new J2eeAdviceAdapter(mv, access, name, desc, signCode, this.context, this.isJakarta);
            this.setTransformed();
        }

//        if (this.hasTransformed()) {
//            DongTaiLog.trace("rewrite method {}.{} for listener[match={}]", new Object[]{this.context.getClassName(), name, this.context.getMatchedClassName()});
//        }

        return mv;
    }

    private boolean isService(String name, Type[] typeOfArgs) {
        if ("service".equals(name)) {
            return this.isServiceArgs(typeOfArgs);
        } else {
            return "doFilter".equals(name) && (this.isFilterArg(typeOfArgs) || this.isFilterChainArg(typeOfArgs));
        }
    }

    private boolean isServiceArgs(Type[] typeOfArgs) {
        return typeOfArgs.length == 2 && this.HTTP_SERVLET_REQUEST.equals(typeOfArgs[0].getClassName()) && this.HTTP_SERVLET_RESPONSE.equals(typeOfArgs[1].getClassName());
    }

    private boolean isJakartaArgs(Type[] typeOfArgs) {
        return typeOfArgs.length == 2 && (this.JAKARTA_SERVLET_REQUEST_HTTP.equals(typeOfArgs[0].getClassName()) && this.JAKARTA_SERVLET_RESPONSE_HTTP.equals(typeOfArgs[1].getClassName()) || this.JAKARTA_SERVLET_REQUEST.equals(typeOfArgs[0].getClassName()) && this.JAKARTA_SERVLET_RESPONSE.equals(typeOfArgs[1].getClassName()));
    }

    private boolean isFacesArgs(Type[] typeOfArgs) {
        if (typeOfArgs.length != 2) {
            return false;
        } else {
            String arg1Classname = typeOfArgs[0].getClassName();
            String arg2Classname = typeOfArgs[1].getClassName();
            return this.SERVLET_REQUEST.equals(arg1Classname) && this.SERVLET_RESPONSE.equals(arg2Classname);
        }
    }

    private boolean isFilterArg(Type[] typeOfArgs) {
        return typeOfArgs.length == 3 && this.SERVLET_REQUEST.equals(typeOfArgs[0].getClassName()) && this.SERVLET_RESPONSE.equals(typeOfArgs[1].getClassName()) && this.FILTER_CHAIN.equals(typeOfArgs[2].getClassName());
    }

    private boolean isFilterChainArg(Type[] typeOfArgs) {
        return typeOfArgs.length == 2 && this.SERVLET_REQUEST.equals(typeOfArgs[0].getClassName()) && this.SERVLET_RESPONSE.equals(typeOfArgs[1].getClassName());
    }
}
