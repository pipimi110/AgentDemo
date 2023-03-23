package top.popko.agentdemo.enhance.plugin;

import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

public interface DispatchPlugin {
    ClassVisitor dispatch(ClassVisitor var1, ClassContext var2, Policy var3);
//    ClassVisitor dispatch(ClassVisitor var1);
}