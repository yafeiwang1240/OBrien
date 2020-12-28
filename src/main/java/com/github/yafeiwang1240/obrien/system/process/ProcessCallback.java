package com.github.yafeiwang1240.obrien.system.process;

/**
 * 进程回调程序
 */
public interface ProcessCallback {

    void stream(String line);

    void exit(int value);
}
