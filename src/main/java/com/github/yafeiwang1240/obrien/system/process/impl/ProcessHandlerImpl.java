package com.github.yafeiwang1240.obrien.system.process.impl;

import com.github.yafeiwang1240.obrien.system.SystemOS;
import com.github.yafeiwang1240.obrien.system.process.ProcessCallback;
import com.github.yafeiwang1240.obrien.system.process.ProcessFactory;
import com.github.yafeiwang1240.obrien.system.process.ProcessHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangyafei
 * @date 2020-12-28
 */
public class ProcessHandlerImpl implements ProcessHandler {

    private AtomicBoolean finish = new AtomicBoolean(false);
    private volatile Process process;
    private AtomicInteger exitValue;

    private String command;
    private ProcessCallback callback;

    public ProcessHandlerImpl(String command, ProcessCallback callback) {
        if (command == null || command.length() == 0) {
            throw new IllegalArgumentException("command cannot empty");
        }
        this.command = command;
        if (callback == null) {
            this.callback = ProcessFactory.newCallback(command);
        } else {
            this.callback = callback;
        }
    }

    @Override
    public boolean finish() {
        return finish.get();
    }

    @Override
    public int kill() {
        if (process == null) return -1;
        SystemOS.killPg(command);
        process.destroy();
        return process.exitValue();
    }

    @Override
    public int exitValue() {
        if (finish.get()) {
            return exitValue.get();
        }
        throw new IllegalThreadStateException("process has not exited");
    }

    @Override
    public void run() {
        try {
            process = new ProcessBuilder(command.split("\\s+"))
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))){
                String line;
                while ((line = reader.readLine()) != null) {
                    callback.stream(line);
                }
            }
            process.waitFor();
            exitValue = new AtomicInteger(process.exitValue());
        } catch (Exception e) {
            exitValue = new AtomicInteger(-1);
            callback.stream(e.getMessage());
        } finally {
            finish.set(true);
            callback.exit(exitValue.get());
            if (process != null) {
                process.destroy();
            }
        }
    }
}
