package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.JsonInitializedCreate;
import com.github.yafeiwang1240.obrien.initialization.impl.StringInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(JsonInitializedCreate.class)
public @interface InitializedJson {
    boolean cover() default false;
    String json();
    String group() default "";
}
