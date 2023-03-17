package top.popko.agentdemo.enhance.asm;


import org.objectweb.asm.commons.Method;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcher;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherHandler;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;

public interface AsmMethods {
    Method SPY_HANDLER$getDispatcher = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcherHandler.class, "getDispatcher");
    Method SPY$enterHttp = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterHttp");
    Method SPY$leaveHttp = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveHttp", Object.class, Object.class);
    Method SPY$isFirstLevelHttp = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isFirstLevelHttp");
    Method SPY$cloneRequest = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "cloneRequest", Object.class, Boolean.TYPE);
    Method SPY$cloneResponse = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "cloneResponse", Object.class, Boolean.TYPE);
    Method SPY$enterSource = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterSource");
    Method SPY$leaveSource = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveSource");
    Method SPY$isFirstLevelSource = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isFirstLevelSource");
    Method SPY$enterPropagator = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterPropagator", Boolean.TYPE);
    Method SPY$leavePropagator = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leavePropagator", Boolean.TYPE);
    Method SPY$isFirstLevelPropagator = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isFirstLevelPropagator");
    Method SPY$enterSink = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "enterSink");
    Method SPY$leaveSink = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "leaveSink");
    Method SPY$isFirstLevelSink = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isFirstLevelSink");
    Method SPY$collectMethodPool = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "collectMethodPool", Object.class, Object[].class, Object.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE, Integer.TYPE);
    Method SPY$collectMethod = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "collectMethod", Object.class, Object[].class, Object.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE);
    Method SPYIMPL$collectMethod = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcherImpl.class, "collectMethod", Object.class, Object[].class, Object.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE);
    Method SPY$reportService = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "reportService", String.class, String.class, String.class, String.class, String.class);
    Method SPY$isReplayRequest = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isReplayRequest");
    Method SPY$isNotReplayRequest = AsmMethods.InnerHelper.getAsmMethod(SpyDispatcher.class, "isNotReplayRequest");


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
