package com.github.yafeiwang1240.obrien.pool;

import com.github.yafeiwang1240.obrien.pool.execption.BeanPoolSizeArgumentException;
import com.github.yafeiwang1240.obrien.pool.model.BaseBean;
import com.github.yafeiwang1240.obrien.pool.model.BeanFactory;

import java.util.concurrent.TimeUnit;

/**
 * bean pool factory
 */
public class BeanPoolFactory {
    public static <T extends BaseBean> BeanPool<T> newBeanPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BeanFactory<T> beanFactory) throws BeanPoolSizeArgumentException {
        return new BeanPool<>(corePoolSize, maximumPoolSize, keepAliveTime, unit, beanFactory);
    }
}
