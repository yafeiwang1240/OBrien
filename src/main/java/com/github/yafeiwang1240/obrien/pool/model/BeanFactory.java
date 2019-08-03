package com.github.yafeiwang1240.obrien.pool.model;

/**
 * bean 工厂类
 * @param <T>
 */
public abstract class BeanFactory<T extends BaseBean> {
    public abstract T newBean();
}
