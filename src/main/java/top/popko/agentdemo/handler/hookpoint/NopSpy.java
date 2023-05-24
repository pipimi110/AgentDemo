package top.popko.agentdemo.handler.hookpoint;

public class NopSpy implements SpyDispatcher {
    public NopSpy() {
    }

    public void enterHttp() {
    }

    public void leaveHttp(Object request, Object response) {
    }

    public boolean isFirstLevelHttp() {
        return false;
    }

    public void enterSource() {
    }

    public void leaveSource() {
    }

    public boolean isFirstLevelSource() {
        return false;
    }

    public void enterPropagator(boolean skipScope) {
    }

    public void leavePropagator(boolean skipScope) {
    }

    public boolean isFirstLevelPropagator() {
        return false;
    }

    public void enterSink() {
    }

    public void leaveSink() {
    }

    public boolean isFirstLevelSink() {
        return false;
    }

    public void reportService(String category, String type, String host, String port, String handler) {
    }

    public boolean isReplayRequest() {
        return false;
    }

    public boolean isNotReplayRequest() {
        return false;
    }

    public boolean collectMethodPool(Object instance, Object[] argumentArray, Object retValue, String framework, String className, String matchClassName, String methodName, String signCode, boolean isStatic, int handlerType) {
        return false;
    }

    public boolean collectMethod(Object instance, Object[] parameters, Object retObject, String methodMatcher, String className, String matchedClassName, String methodName, String signature, boolean isStatic) {
        return false;
    }
}
