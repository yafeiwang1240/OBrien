package com.github.yafeiwang1240.obrien.algorithm.observer;

/**
 * 观察者
 * @param <T>
 */
public interface Observer<T> {
    boolean update(T period);
}
