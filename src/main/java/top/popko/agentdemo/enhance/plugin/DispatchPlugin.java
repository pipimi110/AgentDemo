package top.popko.agentdemo.enhance.plugin;

import org.objectweb.asm.ClassVisitor;

public interface DispatchPlugin {
//    ClassVisitor dispatch(ClassVisitor var1, ClassContext var2, Policy var3);
    ClassVisitor dispatch(ClassVisitor var1);
}