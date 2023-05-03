package top.popko.agentdemo;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.IastClassDiagram;
import top.popko.agentdemo.enhance.plugin.AbstractClassVisitor;
import top.popko.agentdemo.enhance.plugin.PluginManager;
import top.popko.agentdemo.enhance.plugin.core.CoreClassVisitor;
import top.popko.agentdemo.enhance.plugin.core.DispatchClassPlugin;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;
import top.popko.agentdemo.handler.hookpoint.models.policy.PolicyManager;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DefineTransformer implements ClassFileTransformer {
    private Config config;
    private static IastClassDiagram classDiagram = IastClassDiagram.getInstance();

    DefineTransformer(Instrumentation inst) {
    }

    DefineTransformer(Instrumentation inst, Config javassistConfig) {
        this.config = javassistConfig;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        return javassistTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        return asmTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    public boolean tmpClassFilter(String classname) {
        if (classname.startsWith("sun") || classname.startsWith("java") || classname.contains("$")) {
            return false;
        }
        return true;
    }

    public byte[] asmTransform(ClassLoader loader, String internalClassName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        for (String notTransformClass : notTransformList) {
//            if (internalClassName.startsWith(notTransformClass)) {
//                return null;
//            }
//        }
        if (internalClassName == null || internalClassName.startsWith("top/popko/agentdemo")) {
            return null;
        }
        if (null == classBeingRedefined && !ConfigMatcher.getInstance().canHook(internalClassName)) {
            return null;
        }
//        if (!tmpClassFilter(internalClassName)) {//todo: 注释后java.lang.String.concat 重载报错
//            return null;
//        }
        String className = internalClassName.replace("/", ".");
        return adviceAdapterTest1(loader, classfileBuffer);
    }

    public byte[] adviceAdapterTest1(ClassLoader loader, byte[] classfileBuffer) {
        byte[] originalClassfileBuffer = classfileBuffer;
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassContext classContext = new ClassContext(classReader, loader);
        String className = classContext.getClassName();
        Set<String> ancestors = this.classDiagram.getDiagram(className);
        if (ancestors == null) {
            this.classDiagram.setLoader(loader);
            this.classDiagram.saveAncestors(className, classContext.getSuperClassName(), classContext.getInterfaces());
            ancestors = this.classDiagram.getAncestors(className, classContext.getSuperClassName(), classContext.getInterfaces());
        }
        classContext.setAncestors(ancestors);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = PluginManager.getInstance().matchNewClassVisitor(classWriter, classContext, PolicyManager.getInstance().getPolicy());
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);//触发IASTClassVisitor.visitMethod
        //accept()方法满足规则->setTransformed()
        if (classVisitor instanceof AbstractClassVisitor && ((AbstractClassVisitor) classVisitor).hasTransformed()) {
            classfileBuffer = classWriter.toByteArray();
//            System.out.println(String.format("[TEST] 重新加载成功 %s", className));
        } else {
//            System.err.println(String.format("[TEST] 加载失败 %s: hasTransformed", className));
        }
        return classfileBuffer;
    }

    public byte[] javassistTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        if (!LoadCache.check(className)) {
        className = className.replace('/', '.');
//        System.out.println(String.format("[TEST] 检测到加载器 %s 新加载类: %s", loader == null ? "null" : loader.getClass().getName(), className));
//            return PluginManager.check(className, classfileBuffer);
        Iterator it = config.getBlackClassMap().keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (className.equals(key)) {
                byte[] bytess = cookClass(className, config.getBlackClassMap().get(className));
                return bytess == null ? classfileBuffer : bytess;
            }
        }
//        }
//        byte[] bytes = cookMemShell(className);
//        return bytes == null ? classfileBuffer : bytes;
        return classfileBuffer;
    }

    public void retransform(Instrumentation inst) {
        Class[] classes = inst.getAllLoadedClasses();
        Policy policy = PolicyManager.getInstance().getPolicy();
        for (Class aClass : classes) {
//            System.out.println(aClass.getClassLoader());
//            System.out.println(String.format("[TEST] 开始重新加载 %s", aClass.getName()));
            if (inst.isModifiableClass(aClass)) {
                try {
                    if (PolicyManager.isHookClass(aClass.getName()) || (policy != null && policy.isMatchClass(aClass.getName()))) {
                        //jdk8有的类重载retransformClasses()->retransformClasses0()
                        //会报错failed to write core dump.
                        //用规则防止进入
                        inst.retransformClasses(aClass);
                    } else {
//                        System.err.println(String.format("[TEST] 加载失败 %s: isMatchClass", aClass.getName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                System.err.println(String.format("[TEST] 加载失败 %s: isModifiableClass", aClass.getName()));
            }
        }
    }


    public void cookClassField(CtClass clazz, HashMap methodMap) {

    }

    public void cookClassMethod(CtClass clazz, HashMap methodMap) {
        try {
            Iterator it = methodMap.keySet().iterator();
            while (it.hasNext()) {
                String methodSign = (String) it.next();
                String methodName = methodSign.substring(0, methodSign.indexOf(":"));
                String methodDesc = methodSign.substring(methodSign.indexOf(":") + 1);
                String methodBody = (String) methodMap.get(methodSign);
//                CtMethod method = clazz.getDeclaredMethod(methodName);
                CtMethod method = clazz.getMethod(methodName, methodDesc);//getMethod需要desc参数
//                method.setBody(methodBody);
                String[] bodys = methodBody.split("@@", 2);
                if (bodys.length == 1) {
                    method.insertBefore(methodBody);
                } else {
                    method.insertBefore(bodys[0]);
                    method.insertAfter(bodys[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] cookClass(String className, HashMap methodMap) {
        System.err.println(String.format("[TEST] 检测到危险类加载: %s; 开始处理...", className));
        try {
            CtClass clazz = ClassPool.getDefault().get(className);
            cookClassMethod(clazz, methodMap);
//            cookClassField(clazz,methodMap);
            // 返回字节码，并且detachCtClass对象
            byte[] byteCode = clazz.toBytecode();
            //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
            clazz.detach();
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}