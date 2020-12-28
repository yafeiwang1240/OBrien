package com.github.yafeiwang1240.obrien.system.process.impl;


import com.github.yafeiwang1240.obrien.system.process.ProcessCallback;

import java.util.logging.Logger;

/**
 * default stream handler impl
 * @author wangyafei
 */
public class DefaultProcessCallbackImpl implements ProcessCallback {

    private static Logger logger = Logger.getLogger("DefaultProcessCallbackImpl");

    private String name;

    public DefaultProcessCallbackImpl(String name) {
        this.name = name;
    }

    @Override
    public void stream(String line) {
        logger.info(line);
    }

    @Override
    public void exit(int value) {
        if (value != 0) {
            logger.warning(name + " exit with " + value);
        } else {
            logger.info(name + " exit ok");
        }
    }
}
