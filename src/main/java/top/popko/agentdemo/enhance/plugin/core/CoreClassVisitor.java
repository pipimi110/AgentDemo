package top.popko.agentdemo.enhance.plugin.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.MethodContext;
import top.popko.agentdemo.enhance.plugin.AbstractClassVisitor;
import top.popko.agentdemo.enhance.plugin.core.adapter.*;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyNode;
import top.popko.agentdemo.util.AsmUtils;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CoreClassVisitor extends AbstractClassVisitor {
    private int classVersion;

    //                private final MethodAdapter[] methodAdapters = new MethodAdapter[]{new SourceAdapter(), new PropagatorAdapter(), new SinkAdapter()};
    private final TaintAdapter[] taintAdapters = new TaintAdapter[]{new SourceAdapter(), new PropagatorAdapter(), new SinkAdapter()};

    CoreClassVisitor(ClassVisitor classVisitor, ClassContext classContext, Policy policy) {
        super(classVisitor, classContext, policy);
    }

    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        //排除接口 抽象类 静态初始化
        if (!Modifier.isInterface(access) && !Modifier.isAbstract(access) && !"<clinit>".equals(name)) {
            MethodContext methodContext = new MethodContext(this.context, name);
            methodContext.setModifier(access);
            methodContext.setDescriptor(descriptor);
            methodContext.setParameters(AsmUtils.buildParameterTypes(descriptor));
            String matchedSignature = AsmUtils.buildSignature(this.context.getMatchedClassName(), name, descriptor);
//                MethodVisitor mvx = this.lazyAop(mv, access, name, descriptor, signature, methodContext);
            MethodVisitor mvx = this.lazyAop(mv, access, name, descriptor, matchedSignature, methodContext);
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

    private MethodVisitor lazyAop(MethodVisitor mv, int access, String name, String descriptor, String signature, MethodContext methodContext) {
//        private MethodVisitor lazyAop(MethodVisitor mv, int access, String name, String descriptor, String signature) {
//            //方法规则匹配test
//            if ("soutplus".equals(name) && "(Ljava/lang/String;)V".equals(descriptor)) {
////                mv = new MethodAdviceAdapter((MethodVisitor)mv, access, name, descriptor, signature, matchedNodes, methodContext, this.taintAdapters);
//                mv = new MethodAdviceAdapter(mv, access, name, descriptor, signature, this.taintAdapters);
//                this.setTransformed();
//            }

        Set<PolicyNode> matchedNodes = new HashSet();
        Map<String, PolicyNode> policyNodesMap = this.policy.getPolicyNodesMap();
        if (policyNodesMap != null && policyNodesMap.size() != 0) {
            Iterator var9 = policyNodesMap.entrySet().iterator();

            while (var9.hasNext()) {
                Map.Entry<String, PolicyNode> entry = (Map.Entry) var9.next();
                if (entry.getValue().getMethodMatcher().match(methodContext)) {
                    matchedNodes.add(entry.getValue());
                }
            }
        }

        if (matchedNodes.size() > 0) {//只要方法匹配规则，就修改mv
            mv = new MethodAdviceAdapter(mv, access, name, descriptor, signature, matchedNodes, methodContext, this.taintAdapters);
            this.setTransformed();
        }

        return mv;
    }
}
