package com.github.yafeiwang1240.obrien.algorithm.observer;

import java.util.List;

/**
 * 发布者
 * @param <T>
 */
public interface Subject<T> {

    /**
     * 添加观察者
     * @param observer
     */
    default boolean addObserver(Observer observer) {
        return getObserver().add(observer);
    }

    /**
     * 删除观察者,观察者需实现equals方法
     * @param observer
     */
    default boolean deleteObserver(Observer observer) {
        return getObserver().remove(observer);
    }

    /**
     * 获取观察者列表
     * @return
     */
    List<Observer> getObserver();

    /**
     * 通知观察者
     * @param t
     */
    default boolean notifyObserver(T t) {
        if (getObserver() != null) {
            for (Observer observer : getObserver()) {
                if (!observer.update(t)) {
                    return false;
                }
            }
        }
        return true;
    }
}
