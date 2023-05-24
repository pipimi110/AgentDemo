package top.popko.agentdemo.enhance.asm;


import org.objectweb.asm.commons.Method;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcher;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;

public interface AsmMethods {
    Method SPY_HANDLER$getDispatcher = AsmMethods.InnerHelper.getAsmMethod(
            SpyDispatcherHandler.class, "getDispatcher");
    Method SPY$enterHttp = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterHttp");
    Method SPY$leaveHttp = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveHttp", Object.class, Object.class);
    Method SPY$enterSource = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterSource");
    Method SPY$leaveSource = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveSource");
    Method SPY$enterPropagator = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterPropagator", Boolean.TYPE);
    Method SPY$leavePropagator = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leavePropagator", Boolean.TYPE);
    Method SPY$enterSink = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterSink");
    Method SPY$leaveSink = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveSink");
    Method SPY$collectMethodPool = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "collectMethodPool", Object.class, Object[].class, Object.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE, Integer.TYPE);
    //接口也可以查找到相关实例的方法
    Method SPY$collectMethod = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "collectMethod", Object.class, Object[].class, Object.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE);

    public static class InnerHelper {
        private InnerHelper() {
        }

        static Method getAsmMethod(Class<?> clazz, String methodName, Class<?>... parameterClassArray) {
            try {
                return Method.getMethod(clazz.getDeclaredMethod(methodName, parameterClassArray));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
