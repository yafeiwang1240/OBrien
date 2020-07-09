package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.StringInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(StringInitializedCreate.class)
public @interface InitializedString {
    boolean cover() default false;
    String value();
    String group() default "";
}
