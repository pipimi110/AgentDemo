package top.popko.agentdemo.handler.hookpoint.controller.wrapper;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ServletRequestWrapper extends HttpServletRequestWrapper implements DongTaiRequest {
    private String body;
    private final boolean usingBody;
    private final boolean isPostMethod = "POST".equals(this.getMethod());
    private boolean isCachedBody;

    public ServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.usingBody = this.isPostMethod && this.allowedContentType(request.getContentType());
    }

    public ServletInputStream getInputStream() throws IOException {
        if (!this.usingBody) {
            return super.getInputStream();
        } else {
            if (!this.isCachedBody) {
                InputStream inputStream = super.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = null;

                try {
                    if (inputStream != null) {
                        String ce = this.getCharacterEncoding();
                        if (null != ce && !ce.isEmpty()) {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ce));
                        } else {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        }

                        char[] charBuffer = new char[128];

                        int bytesRead;
                        while((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                            stringBuilder.append(charBuffer, 0, bytesRead);
                        }
                    }

                    assert bufferedReader != null;

                    bufferedReader.close();
                } catch (IOException var7) {
//                    DongTaiLog.error("io.dongtai.api.servlet2.ServletRequestWrapper.getInputStream()", var7);
                }

                this.body = stringBuilder.toString();
                this.isCachedBody = true;
            }

            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body.getBytes());
            return new ServletInputStream() {
                public boolean isFinished() {
                    return false;
                }

                public boolean isReady() {
                    return false;
                }

                public void setReadListener(ReadListener readListener) {
                }

                public int read() {
                    return byteArrayInputStream.read();
                }
            };
        }
    }

    public BufferedReader getReader() throws IOException {
        return this.usingBody ? new BufferedReader(new InputStreamReader(this.getInputStream())) : super.getReader();
    }

    public Map<String, Object> getRequestMeta() {
        Map<String, Object> requestMeta = new HashMap(16);
        requestMeta.put("contextPath", this.getContextPath());
        requestMeta.put("servletPath", this.getServletPath());
        requestMeta.put("requestURL", this.getRequestURL());
        requestMeta.put("requestURI", this.getRequestURI());
        requestMeta.put("method", this.getMethod());
        requestMeta.put("serverName", this.getServerName());
        requestMeta.put("serverPort", this.getServerPort());
        requestMeta.put("queryString", this.getQueryString());
        requestMeta.put("protocol", this.getProtocol());
        requestMeta.put("scheme", this.getScheme());
        requestMeta.put("remoteAddr", this.getDongTaiRemoteAddr());
        requestMeta.put("secure", this.isSecure());
        requestMeta.put("body", "");
        requestMeta.put("headers", this.getHeaders());
        requestMeta.put("replay-request", null != this.getHeader("dongtai-replay-id"));
        return requestMeta;
    }

    public Map<String, String> getHeaders() {
        Enumeration<?> headerNames = this.getHeaderNames();
        Map<String, String> headers = new HashMap(32);

        while(headerNames.hasMoreElements()) {
            String name = (String)headerNames.nextElement();
            String value = this.getHeader(name);
            headers.put(name, value);
        }

        return headers;
    }

    public String getPostBody() {
        StringBuilder postBody = new StringBuilder();

        try {
            if (!this.isPostMethod) {
                return postBody.toString();
            }

            if (this.isCachedBody) {
                return this.body;
            }

            if (this.usingBody) {
                this.getInputStream();
                return this.body;
            }

            Enumeration<?> parameterNames = this.getParameterNames();
            boolean first = true;

            while(parameterNames.hasMoreElements()) {
                String param = (String)parameterNames.nextElement();
                if (first) {
                    first = false;
                    postBody.append(param).append("=").append(this.getParameter(param));
                } else {
                    postBody.append("&").append(param).append("=").append(this.getParameter(param));
                }
            }
        } catch (IOException var5) {
//            DongTaiLog.error(var5);
        }

        return postBody.toString();
    }

    public String getDongTaiRemoteAddr() {
        String remoteAddr = super.getRemoteAddr();
        return remoteAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : remoteAddr;
    }
}
