package top.popko.agentdemo.handler.hookpoint;

public class SpyDispatcherHandler {
    private static final SpyDispatcher nopSpy = new NopSpy();
    private static SpyDispatcher dispatcher;

    public SpyDispatcherHandler() {
    }

    public static void setDispatcher(SpyDispatcher dispatcher) {
        SpyDispatcherHandler.dispatcher = dispatcher;
    }

    public static SpyDispatcher getDispatcher() {
        return dispatcher;
    }

    public static void destroy() {
        setDispatcher(nopSpy);
    }
}
