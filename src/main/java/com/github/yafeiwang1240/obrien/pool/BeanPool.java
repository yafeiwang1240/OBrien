package com.github.yafeiwang1240.obrien.pool;


import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.pool.execption.BeanPoolSizeArgumentException;
import com.github.yafeiwang1240.obrien.pool.model.BaseBean;
import com.github.yafeiwang1240.obrien.pool.model.BeanFactory;
import com.github.yafeiwang1240.obrien.pool.model.BeanMonitor;
import com.github.yafeiwang1240.obrien.pool.model.BeanPack;
import com.github.yafeiwang1240.obrien.uitls.IOUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * bean pool
 * @param <T>
 */
public class BeanPool<T extends BaseBean> {

    // 缓冲时间
    private static long BumperTime = 3141;
    private List<BeanPack<T>> pool;
    private ExecutorService service;
    private Object[] lock;
    private PoolMonitor poolMonitor;

    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
    private TimeUnit unit;
    private BeanFactory<T> beanFactory;

    public BeanPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BeanFactory<T> beanFactory) throws BeanPoolSizeArgumentException {
        if (corePoolSize <= 0 || maximumPoolSize < corePoolSize) {
            throw new BeanPoolSizeArgumentException("argument with corePoolSize: " + corePoolSize + ", maximumPoolSize: " + maximumPoolSize);
        }
        pool = Lists.newArrayList(maximumPoolSize);
        service = Executors.newSingleThreadExecutor();
        lock = new Object[0];
        poolMonitor = new PoolMonitor();

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.beanFactory = beanFactory;

        service.execute(poolMonitor);
    }

    /**
     * 从bean pool中获取bean, 若pool中无空闲返回空
     * @return
     */
    public T getBean() {
        return getBean(3, TimeUnit.MINUTES);
    }

    public T getBean(long timeout, TimeUnit unit) {
        long now = System.currentTimeMillis();
        long space = unit.toMillis(timeout);
        T bean = null;
        while ((System.currentTimeMillis() - now) < space) {
            synchronized (lock) {
                BeanMonitor monitor = null;
                for (BeanPack p : pool) {
                    if (!p.getBeanMonitor().isInuse()) {
                        monitor = p.getBeanMonitor();
                        bean = (T) p.getBean();
                        break;
                    }
                }
                if (monitor == null && pool.size() < maximumPoolSize) {
                    BeanPack pack = new BeanPack();
                    monitor = new BeanMonitor();
                    bean = beanFactory.newBean();
                    pack.setBeanMonitor(monitor);
                    pack.setBean(bean);
                    pool.add(pack);
                }
                if (monitor != null) {
                    monitor.setInuse(true);
                }
            }
            if (bean != null) return bean;
            IOUtils.sleep(300);
        }
        return null;
    }

    private class PoolMonitor implements Runnable {

        private boolean exit;

        @Override
        public void run() {
            while (!exit) {
                synchronized (lock) {
                    if (pool.size() > corePoolSize) {
                        long now = System.currentTimeMillis();
                        for (BeanPack p : pool) {
                            if (!p.getBeanMonitor().isInuse()) {
                                // 未使用超过最长存活时间 + 缓冲时间
                                if (now - unit.toMillis(keepAliveTime) - BumperTime > p.getBeanMonitor().getTimestamp()) {
                                    pool.remove(p);
                                    break;
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(BumperTime);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        public void stop() {
            exit = true;
        }
    }
}
