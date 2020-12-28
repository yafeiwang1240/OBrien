package com.github.yafeiwang1240.obrien.system.process;

import com.github.yafeiwang1240.obrien.system.process.impl.DefaultProcessCallbackImpl;
import com.github.yafeiwang1240.obrien.system.process.impl.ProcessHandlerImpl;

/**
 * 进程句柄工厂类
 * @author wangyafei
 */
public class ProcessFactory {

    /**
     * get default callback
     * @param name
     * @return
     */
    public static ProcessCallback newCallback(String name) {
        return new DefaultProcessCallbackImpl(name);
    }

    /**
     * get new ProcessHandler
     * @param command
     * @param callback
     * @return
     */
    public static ProcessHandler newProcessHandler(String command, ProcessCallback callback) {
        return new ProcessHandlerImpl(command, callback);
    }

    /**
     * new process handler without callback
     * @param command
     * @return
     */
    public static ProcessHandler newProcessHandler(String command) {
        return new ProcessHandlerImpl(command, null);
    }
}
