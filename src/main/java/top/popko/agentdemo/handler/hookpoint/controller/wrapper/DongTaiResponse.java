package top.popko.agentdemo.handler.hookpoint.controller.wrapper;
import java.io.IOException;
import java.util.Map;

public interface DongTaiResponse {
    Map<String, Object> getResponseMeta(boolean var1);

    byte[] getResponseData() throws IOException;
}
