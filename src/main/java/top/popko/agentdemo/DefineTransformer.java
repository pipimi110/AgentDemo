package top.popko.agentdemo;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import top.popko.agentdemo.config.ConfigMatcher;
import top.popko.agentdemo.enhance.plugin.core.DispatchClassPlugin;
import top.popko.agentdemo.handler.hookpoint.IASTClassVisitor;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcher;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;

public class DefineTransformer implements ClassFileTransformer {
    private Config config;
    private ConfigMatcher configMatcher;

    DefineTransformer(Instrumentation inst) {
        this.configMatcher = ConfigMatcher.getInstance();
        this.configMatcher.setInst(inst);
        SpyDispatcherHandler.setDispatcher(new SpyDispatcherImpl());
    }
    DefineTransformer(Instrumentation inst,Config javassistConfig) {
        this.config = javassistConfig;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        return javassistTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        return asmTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    public boolean tmpClassFilter(String classname){
        if(classname.startsWith("sun")||classname.startsWith("java")){
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
        if(internalClassName == null || internalClassName.startsWith("top/popko/agentdemo")){
            return null;
        }
        if (null == classBeingRedefined && !this.configMatcher.canHook(internalClassName)) {
            return null;
        }
        if (!tmpClassFilter(internalClassName)){
            return null;
        }
        String className = internalClassName.replace("/", ".");
        return adviceAdapterTest1(classfileBuffer, className);
    }

    public byte[] adviceAdapterTest1(byte[] classfileBuffer, String className) {
        byte[] originalClassfileBuffer = classfileBuffer;
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor classVisitor = new IASTClassVisitor(className, classWriter);
        DispatchClassPlugin dispatchClassPlugin = new DispatchClassPlugin();
        ClassVisitor classVisitor = dispatchClassPlugin.dispatch(classWriter);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);//触发IASTClassVisitor.visitMethod
        classfileBuffer = classWriter.toByteArray();
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
        for (Class aClass : classes) {
            if (inst.isModifiableClass(aClass)) {
//                System.out.println(String.format("[TEST] 重新加载"));
                try {
                    inst.retransformClasses(aClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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