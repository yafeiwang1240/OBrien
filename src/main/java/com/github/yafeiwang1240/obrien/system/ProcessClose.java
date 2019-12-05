package com.github.yafeiwang1240.obrien.system;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 简化Process代码结构
 * @author wangyafei
 */
public class ProcessClose extends Process implements Closeable {

    private Process process;

    public ProcessClose(Process process) {
        this.process = process;
    }

    @Override
    public void close() throws IOException {
        if (process != null) process.destroy();
    }

    @Override
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    @Override
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    @Override
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }

    @Override
    public int exitValue() {
        return process.exitValue();
    }

    @Override
    public void destroy() {
        process.destroy();
    }
}
