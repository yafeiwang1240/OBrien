package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;

import java.lang.reflect.Field;

public class FieldInitializedExecutor {
    private Field field;
    private Initialized initialized;

    public FieldInitializedExecutor(Field field, Initialized initialized) {
        this.field = field;
        this.initialized = initialized;
        this.field.setAccessible(true);
    }

    public void process(Object o) throws InitializedFailedException {
        try {
            field.set(o, initialized.execute(field.get(o)));
        } catch (IllegalAccessException e) {
            throw new InitializedFailedException("初始化非法访问", e);
        }
    }
}
