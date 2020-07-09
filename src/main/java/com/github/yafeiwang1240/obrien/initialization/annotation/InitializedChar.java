package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.CharacterInitializedCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@InitializedMeans(CharacterInitializedCreate.class)
public @interface InitializedChar {
    boolean cover() default false;
    char value();
    String group() default "";
}
