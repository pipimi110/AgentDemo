package top.popko.agentdemo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtils {
    public static StringBuilder sendGet(String uri) throws Exception {
//        String address = "http://www.baidu.com";
        URL url = new URL(uri);//创建一个URL对象
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();//返回HttpURLConnection类的实例
        //URLConnection urlCon = url.openConnection();//返回URLConnection类的实例
        urlcon.connect(); // 获取连接
        InputStream is = urlcon.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String l = null;
        while ((l = buffer.readLine()) != null) {
            response.append(l).append("\n");
        }
        return response;
    }



    public static void main(String[] args) throws Exception {
        String uri = "http://example.com/api";
        String requestBody = "{\"name\": \"John\", \"age\": 30}";
        sendPOST(uri, requestBody);
    }

    public static StringBuilder sendPOST(String uri, String requestBody) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int status = con.getResponseCode();
        StringBuilder response = new StringBuilder();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Request failed with HTTP error code: " + status);
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String responseBody = response.toString();
                System.out.println("Response body: " + responseBody);
            }
        }
        con.disconnect();
        return response;
    }
}
