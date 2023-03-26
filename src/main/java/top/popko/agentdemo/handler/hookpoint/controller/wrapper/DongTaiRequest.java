package top.popko.agentdemo.handler.hookpoint.controller.wrapper;

import java.util.Map;

public interface DongTaiRequest {
    Map<String, Object> getRequestMeta();

    String getPostBody();

    default boolean allowedContentType(String contentType) {
        return contentType != null && (contentType.contains("application/json") || contentType.contains("application/xml"));
    }
}
