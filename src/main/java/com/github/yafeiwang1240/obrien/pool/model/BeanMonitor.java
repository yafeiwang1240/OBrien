package com.github.yafeiwang1240.obrien.pool.model;

public class BeanMonitor {
    private boolean inuse;
    private long timestamp;

    public boolean isInuse() {
        return inuse;
    }

    public void setInuse(boolean inuse) {
        this.inuse = inuse;
        timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
