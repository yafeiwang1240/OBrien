package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.FloatInitializedCreate;
import com.github.yafeiwang1240.obrien.initialization.impl.IntInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(FloatInitializedCreate.class)
public @interface InitializedFloat {
    boolean cover() default false;
    float value();
    String group() default "";
}
