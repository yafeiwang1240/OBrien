package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.DoubleInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(DoubleInitializedCreate.class)
public @interface InitializedDouble {
    boolean cover() default false;
    double value();
    String group() default "";
}
