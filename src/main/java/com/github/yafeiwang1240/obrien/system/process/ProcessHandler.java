package com.github.yafeiwang1240.obrien.system.process;

/**
 * 进程句柄
 * @date 2020-12-28
 * @author wangyafei
 */
public interface ProcessHandler extends Runnable {

    /**
     * is finish
     * @return true when the process finished
     */
    boolean finish();

    /**
     * kill the process
     * @return
     */
    int kill();

    /**
     * get exit value
     * @return
     */
    int exitValue();
}
