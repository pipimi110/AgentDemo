package top.popko.agentdemo.handler.hookpoint.controller.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ServletWrapperOutputStreamCopier extends ServletOutputStream {
    private final OutputStream out;
    private final ByteArrayOutputStream copier;

    ServletWrapperOutputStreamCopier(OutputStream out) {
        this.out = out;
        this.copier = new ByteArrayOutputStream();
    }

    public void write(int b) throws IOException {
        this.out.write(b);
        this.copier.write(b);
    }

    byte[] getCopy() {
        return this.copier.toByteArray();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {
    }
}
