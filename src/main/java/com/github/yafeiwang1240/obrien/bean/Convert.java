package com.github.yafeiwang1240.obrien.bean;

/**
 * java bean tools
 */
public class Convert {

    public <T> T to(Class<T> tClass) throws InstantiationException, IllegalAccessException {
        T t =tClass.newInstance();
        copyTo(t);
        return t;
    }

    public void copyTo(Object object) {
        BeanUtils.copy(this, object);
    }

    public void copyFrom(Object object) {
        BeanUtils.copy(object, this);
    }

}
