package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.impl.AbstractInitializedCreate;
import com.github.yafeiwang1240.obrien.initialization.impl.UserInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性初始化
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(UserInitializedCreate.class)
public @interface Initializer {
    Class<? extends Initialized>[] initializations();
}
