package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.uitls.JsonUtils;

import java.lang.reflect.Field;

public class JsonInitialized<T> implements Initialized<T> {

    private boolean cover;
    private String value;
    private Class<?> clazz;

    public JsonInitialized(String value) {
        this(value, false);
    }

    public JsonInitialized(String value, boolean cover) {
        this.cover = cover;
        this.value = value;
    }

    @Override
    public T execute(T data) {
        if (cover || data == null || data.toString().trim().length() == 0) {
            return (T) JsonUtils.parseObject(value, clazz);
        }
        return data;
    }

    @Override
    public void init(Field field) {
        clazz = field.getType();
    }
}
