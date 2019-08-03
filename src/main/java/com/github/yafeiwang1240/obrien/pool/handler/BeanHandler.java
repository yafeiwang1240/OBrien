package com.github.yafeiwang1240.obrien.pool.handler;

import com.github.yafeiwang1240.obrien.pool.execption.BeanHandlerException;

public interface BeanHandler<T> {
    void handler(T s) throws BeanHandlerException;
}
