package top.popko.agentdemo.enhance.asm;

import org.objectweb.asm.Type;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcher;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;


public interface AsmTypes {
    Type ASM_TYPE_SPY_DISPATCHER = Type.getType(SpyDispatcher.class);
    Type ASM_TYPE_SPY_HANDLER = Type.getType(SpyDispatcherHandler.class);
    Type ASM_TYPE_THROWABLE = Type.getType(Throwable.class);
    Type ASM_TYPE_OBJECT = Type.getType(Object.class);
}
