package com.github.yafeiwang1240.obrien.pool.model;

import com.github.yafeiwang1240.obrien.pool.handler.BeanHandler;

/**
 * 抽象基类，新创建bean需继承此类，便于直接回收
 */
public abstract class BaseBean {

    private BeanHandler<Boolean> handler;

    final BeanHandler<Boolean> getHandler() {
        return handler;
    }

    final void setHandler(BeanHandler<Boolean> handler) {
        this.handler = handler;
    }

    /**
     * 回收资源，bean使用完毕之后需调用此函数
     */
    public final void release() {
        try {
            handler.handler(false);
        } catch (Exception e) {
            // 内部逻辑，忽略异常
            e.printStackTrace();
        }
    }
}
