package com.github.yafeiwang1240.obrien.pool.model;

/**
 * 抽象基类，新创建bean需继承此类，便于直接回收
 */
public abstract class BaseBean {

    private BeanMonitor monitor;

    final void setMonitor(BeanMonitor monitor) {
        this.monitor = monitor;
    }

    final BeanMonitor getMonitor() {
        return monitor;
    }

    /**
     * 回收资源，bean使用完毕之后需调用此函数
     */
    public final void release() {
        monitor.setInuse(false);
    }
}
