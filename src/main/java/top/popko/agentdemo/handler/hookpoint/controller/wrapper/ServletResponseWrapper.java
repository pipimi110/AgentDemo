//package top.popko.agentdemo.handler.hookpoint.controller.wrapper;
//
//
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponseWrapper;
//
//public class ServletResponseWrapper extends HttpServletResponseWrapper implements DongTaiResponse {
//    private ServletOutputStream outputStream = null;
//    private PrintWriter writer = null;
//    private ServletWrapperOutputStreamCopier copier = null;
//
//    public ServletResponseWrapper(HttpServletResponse response) {
//        super(response);
//
////        try {
////            boolean enableVersionHeader = (Boolean)ConfigBuilder.getInstance().getConfig(ConfigKey.ENABLE_VERSION_HEADER).get();
////            if (enableVersionHeader) {
////                String versionHeaderKey = (String)ConfigBuilder.getInstance().getConfig(ConfigKey.VERSION_HEADER_KEY).get();
////                response.addHeader(versionHeaderKey, "v1.8.1");
////            }
////        } catch (Throwable var4) {
////        }
//
//    }
//
//    private String getLine() {
//        return "HTTP/1.1 " + this.getStatus() + "\n";
//    }
//
//    public ServletOutputStream getOutputStream() throws IOException {
//        if (this.writer != null) {
////            DongTaiLog.error("getOutputStream() has already been called over once");
//        }
//
//        if (this.outputStream == null) {
//            this.outputStream = this.getResponse().getOutputStream();
//            this.copier = new ServletWrapperOutputStreamCopier(this.outputStream);
//        }
//
//        return this.copier;
//    }
//
//    public PrintWriter getWriter() throws IOException {
//        if (this.outputStream != null) {
////            DongTaiLog.error("getWriter() has already been called over once");
//        }
//
//        if (this.writer == null) {
//            this.copier = new ServletWrapperOutputStreamCopier(this.getResponse().getOutputStream());
//            this.writer = new PrintWriter(new OutputStreamWriter(this.copier, this.getResponse().getCharacterEncoding()), true);
//        }
//
//        return this.writer;
//    }
//
//    public void flushBuffer() throws IOException {
//        if (this.writer != null) {
//            this.writer.flush();
//        } else if (this.copier != null) {
//            this.copier.flush();
//        }
//
//    }
//
//    public Map<String, Object> getResponseMeta(boolean getBody) {
//        Map<String, Object> responseMeta = new HashMap(2);
//        responseMeta.put("headers", this.getHeaders());
//        if (getBody) {
//            responseMeta.put("body", this.getResponseData());
//        }
//
//        return responseMeta;
//    }
//
//    private String getHeaders() {
//        StringBuilder header = new StringBuilder();
//        Collection<String> headerNames = this.getHeaderNames();
//        header.append(this.getLine());
//        Iterator var3 = headerNames.iterator();
//
//        while(var3.hasNext()) {
//            String headerName = (String)var3.next();
//            header.append(headerName).append(":").append(this.getHeader(headerName)).append("\n");
//        }
//
//        return header.toString();
//    }
//
//    public byte[] getResponseData() {
//        try {
//            this.flushBuffer();
//        } catch (IOException var2) {
////            DongTaiLog.error(var2);
//        }
//
//        return this.copier != null ? this.copier.getCopy() : new byte[0];
//    }
//}
