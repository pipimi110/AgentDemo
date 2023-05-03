package top.popko.agentdemo.handler.hookpoint.graph;

import org.json.JSONArray;
import org.json.JSONObject;
import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.handler.hookpoint.controller.impl.HttpImpl;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.util.HttpClientUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GraphBuilder {
    private static String URL;
    private static String URI;

    public GraphBuilder() {
    }

    public static void buildAndReport(Object request, Object response) {
        try {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().enterAgent();
            List<GraphNode> nodeList = build();
            if (nodeList.size() != 0) {
                String report = convertToReport(nodeList, request, response);
                System.out.println("[+]report:");
                System.out.println(report);
                HttpClientUtils.sendPOST(System.getProperty("serverhost")+"/iast/add_vulns",report);
                // 发给服务器
            }
//            ThreadPools.sendPriorityReport("/api/v1/report/upload", report);
        } catch (Throwable var7) {
//            DongTaiLog.error("report request failed", var7);
        } finally {
//            ScopeManager.SCOPE_TRACKER.getPolicyScope().leaveAgent();
        }

    }

    public static List<GraphNode> build() {
        List<GraphNode> nodeList = new ArrayList();
        Map<Integer, MethodEvent> events = (Map) EngineManager.TRACK_MAP.get();
        if (events == null) {
            return nodeList;
        }
        Iterator var2 = events.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<Integer, MethodEvent> entry = (Map.Entry) var2.next();
            nodeList.add(new GraphNode((MethodEvent) entry.getValue()));
        }

        return nodeList;
    }

    public static String convertToReport(List<GraphNode> nodeList, Object request, Object response) {
        Map<String, Object> requestMeta = (Map) EngineManager.REQUEST_CONTEXT.get();
        Map<String, Object> responseMeta = response == null ? null : HttpImpl.getResponseMeta(response);
        JSONObject report = new JSONObject();
        JSONObject detail = new JSONObject();
        JSONArray methodPool = new JSONArray();
        report.put("type", 36);
        report.put("version", "v3");
        report.put("detail", detail);
//        detail.put("agentId", EngineManager.getAgentId());
        detail.put("agentId", 2233);
        detail.put("protocol", requestMeta.getOrDefault("protocol", "unknown"));
        detail.put("scheme", requestMeta.getOrDefault("scheme", ""));
        detail.put("method", requestMeta.getOrDefault("method", ""));
        detail.put("secure", requestMeta.getOrDefault("secure", ""));
        String requestURL = requestMeta.getOrDefault("requestURL", "").toString();
        if (null == requestURL) {
            return null;
        } else {
            detail.put("url", requestURL);
            String requestURI = requestMeta.getOrDefault("requestURI", "").toString();
            if (null == requestURI) {
                return null;
            } else {
                detail.put("uri", requestURI);
                setURL(requestURL);
                setURI(requestURI);
                detail.put("clientIp", requestMeta.getOrDefault("remoteAddr", ""));
                detail.put("queryString", requestMeta.getOrDefault("queryString", ""));
//                detail.put("reqHeader", AbstractNormalVulScan.getEncodedHeader((Map)requestMeta.getOrDefault("headers", new HashMap())));
//                detail.put("reqBody", request == null ? "" : HttpImpl.getPostBody(request));
//                detail.put("resHeader", responseMeta == null ? "" : Base64Encoder.encodeBase64String(responseMeta.getOrDefault("headers", "").toString().getBytes()).replaceAll("\n", ""));
//                detail.put("resBody", responseMeta == null ? "" : Base64Encoder.encodeBase64String(getResponseBody(responseMeta)));
                detail.put("contextPath", requestMeta.getOrDefault("contextPath", ""));
                detail.put("replayRequest", requestMeta.getOrDefault("replay-request", false));
                detail.put("pool", methodPool);
                Iterator var10 = nodeList.iterator();

                while (var10.hasNext()) {
                    GraphNode node = (GraphNode) var10.next();
                    methodPool.put(node.toJson());
                }

                return report.toString();
            }
        }
    }

    private static byte[] getResponseBody(Map<String, Object> responseMeta) {
        try {
//            Integer responseLength = PropertyUtils.getInstance().getResponseLength();
            Integer responseLength = 1234;
            byte[] responseBody = (byte[]) ((byte[]) responseMeta.getOrDefault("body", new byte[0]));
            if (responseLength > 0) {
                byte[] newResponseBody = new byte[responseLength];
                newResponseBody = Arrays.copyOfRange(responseBody, 0, responseLength);
                return newResponseBody;
            } else {
                return responseLength == 0 ? new byte[0] : responseBody;
            }
        } catch (Throwable var4) {
            return new byte[0];
        }
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        GraphBuilder.URL = URL;
    }

    public static String getURI() {
        return URI;
    }

    public static void setURI(String URI) {
        GraphBuilder.URI = URI;
    }
}
