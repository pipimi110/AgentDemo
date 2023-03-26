package top.popko.agentdemo.handler.hookpoint.models.track;

import java.util.Map;

public class RequestContext extends ThreadLocal<Map<String, Object>> {
    public RequestContext() {
    }

    protected Map<String, Object> initialValue() {
        return null;
    }

    public String getCookieValue() {
        return (String)((Map)this.get()).get("cookies");
    }
}
