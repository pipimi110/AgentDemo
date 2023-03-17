package top.popko.agentdemo.handler.hookpoint;


public interface SpyDispatcher {
    void enterHttp();

    void leaveHttp(Object var1, Object var2);

    boolean isFirstLevelHttp();

    Object cloneRequest(Object var1, boolean var2);

    Object cloneResponse(Object var1, boolean var2);

    void enterSource();

    void leaveSource();

    boolean isFirstLevelSource();

    void enterPropagator(boolean var1);

    void leavePropagator(boolean var1);

    boolean isFirstLevelPropagator();

    void enterSink();

    void leaveSink();

    boolean isFirstLevelSink();

    void reportService(String var1, String var2, String var3, String var4, String var5);

    boolean isReplayRequest();

    boolean isNotReplayRequest();

    boolean collectMethodPool(Object var1, Object[] var2, Object var3, String var4, String var5, String var6, String var7, String var8, boolean var9, int var10);

    boolean collectMethod(Object var1, Object[] var2, Object var3, String var4, String var5, String var6, String var7, String var8, boolean var9);
}
