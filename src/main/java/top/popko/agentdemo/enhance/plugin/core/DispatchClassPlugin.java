package top.popko.agentdemo.enhance.plugin.core;

import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.DispatchPlugin;
import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

import java.util.Set;

public class DispatchClassPlugin implements DispatchPlugin {
    private Set<String> ancestors;
    private String className;


    public DispatchClassPlugin() {
    }

    public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext classContext, Policy policy) {
//        public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext classContext) {
//    public ClassVisitor dispatch(ClassVisitor classVisitor) {
        this.ancestors = classContext.getAncestors();
        this.className = classContext.getClassName();
        String matchedClassName = policy.getMatchedClass(this.className, this.ancestors);
        if (null == matchedClassName) {
            return classVisitor;
        } else {
            classContext.setMatchedClassName(matchedClassName);
            return new CoreClassVisitor(classVisitor, classContext, policy);
        }
    }

    //    public class ClassVisit extends AbstractClassVisitor {
//    ClassVisit改成CoreClassVisitor

}
