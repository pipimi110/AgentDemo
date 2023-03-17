package top.popko.agentdemo.enhance.plugin.core;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import top.popko.agentdemo.enhance.plugin.DispatchPlugin;
import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.plugin.core.adapter.MethodAdapter;
import top.popko.agentdemo.enhance.plugin.core.adapter.MethodAdviceAdapter;
import top.popko.agentdemo.enhance.plugin.core.adapter.SourceAdapter;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DispatchClassPlugin implements DispatchPlugin {
    private Set<String> ancestors;
    private String className;

    public DispatchClassPlugin() {
    }

//    public ClassVisitor dispatch(ClassVisitor classVisitor, ClassContext classContext, Policy policy) {
    public ClassVisitor dispatch(ClassVisitor classVisitor) {
//        this.ancestors = classContext.getAncestors();
//        this.className = classContext.getClassName();
//        String matchedClassName = policy.getMatchedClass(this.className, this.ancestors);
//        if (null == matchedClassName) {
//            return classVisitor;
//        } else {
//            classContext.setMatchedClassName(matchedClassName);
//            return new ClassVisit(classVisitor, classContext, policy);
//        }
            return new ClassVisit(classVisitor);
    }

//    public class ClassVisit extends AbstractClassVisitor {
    public class ClassVisit extends ClassVisitor {
        private int classVersion;
//        private final MethodAdapter[] methodAdapters = new MethodAdapter[]{new SourceAdapter(), new PropagatorAdapter(), new SinkAdapter()};
        private final MethodAdapter[] methodAdapters = new MethodAdapter[]{new SourceAdapter()};

        ClassVisit(ClassVisitor classVisitor) {
            super(Opcodes.ASM5, classVisitor);
        }
//        ClassVisit(ClassVisitor classVisitor, ClassContext classContext, Policy policy) {
//            super(classVisitor, classContext, policy);
//        }

        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            //排除接口 抽象类 静态初始化
            if (!Modifier.isInterface(access) && !Modifier.isAbstract(access) && !"<clinit>".equals(name)) {
//                MethodContext methodContext = new MethodContext(this.context, name);
//                methodContext.setModifier(access);
//                methodContext.setDescriptor(descriptor);
//                methodContext.setParameters(AsmUtils.buildParameterTypes(descriptor));
//                String matchedSignature = AsmUtils.buildSignature(this.context.getMatchedClassName(), name, descriptor);
                MethodVisitor mvx = this.lazyAop(mv, access, name, descriptor, signature);
//                MethodVisitor mvx = this.lazyAop(mv, access, name, descriptor, matchedSignature, methodContext);
//                boolean methodIsTransformed = mvx instanceof MethodAdviceAdapter;
//                if (methodIsTransformed && this.classVersion < 50) {
//                    mvx = new JSRInlinerAdapter((MethodVisitor)mvx, access, name, descriptor, signature, exceptions);
//                }

//                if (methodIsTransformed) {
//                    DongTaiLog.trace("rewrite method {} for listener[class={}]", new Object[]{matchedSignature, this.context.getClassName()});
//                }

                return mvx;
            } else {
                return mv;
            }
        }

//        private MethodVisitor lazyAop(MethodVisitor mv, int access, String name, String descriptor, String signature, MethodContext methodContext) {
        private MethodVisitor lazyAop(MethodVisitor mv, int access, String name, String descriptor, String signature) {
            //方法规则匹配
            if("soutplus".equals(name) && "(Ljava/lang/String;)V".equals(descriptor)){
//                mv = new MethodAdviceAdapter((MethodVisitor)mv, access, name, descriptor, signature, matchedNodes, methodContext, this.methodAdapters);
                mv = new MethodAdviceAdapter((MethodVisitor)mv, access, name, descriptor, signature, this.methodAdapters);
            }
//            Set<PolicyNode> matchedNodes = new HashSet();
//            Map<String, PolicyNode> policyNodesMap = this.policy.getPolicyNodesMap();
//            if (policyNodesMap != null && policyNodesMap.size() != 0) {
//                Iterator var9 = policyNodesMap.entrySet().iterator();
//
//                while(var9.hasNext()) {
//                    Map.Entry<String, PolicyNode> entry = (Map.Entry)var9.next();
//                    if (((PolicyNode)entry.getValue()).getMethodMatcher().match(methodContext)) {
//                        matchedNodes.add(entry.getValue());
//                    }
//                }
//            }
//
//            if (matchedNodes.size() > 0) {
//                mv = new MethodAdviceAdapter((MethodVisitor)mv, access, name, descriptor, signature, matchedNodes, methodContext, this.methodAdapters);
//                this.setTransformed();
//            }

            return (MethodVisitor)mv;
        }
    }
}
