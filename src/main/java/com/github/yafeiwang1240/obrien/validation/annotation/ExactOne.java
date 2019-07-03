package com.github.yafeiwang1240.obrien.validation.annotation;

import com.github.yafeiwang1240.obrien.validation.impl.ExactOneValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(ExactOneValidator.class)
public @interface ExactOne {
    String[] fields();
    String message();
    String group() default "";
}
