package com.github.yafeiwang1240.obrien.pool.model;

/**
 * bean 工厂类
 * @param <T>
 */
public interface BeanFactory<T extends BaseBean> {
    T newBean();
}
