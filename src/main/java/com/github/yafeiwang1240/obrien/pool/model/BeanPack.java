package com.github.yafeiwang1240.obrien.pool.model;

public class BeanPack<T extends BaseBean> {
    private T bean;
    private BeanMonitor beanMonitor;

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
        if (bean.getHandler() == null) {
            bean.setHandler(_value -> beanMonitor.setInuse(_value));
        }
    }

    public BeanMonitor getBeanMonitor() {
        return beanMonitor;
    }

    public void setBeanMonitor(BeanMonitor beanMonitor) {
        this.beanMonitor = beanMonitor;
    }
}
