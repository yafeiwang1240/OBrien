package com.githup.yafeiwang1240.obrien.validation.annotation;

import com.githup.yafeiwang1240.obrien.validation.impl.StringInValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(StringInValidator.class)
public @interface StringIn {
    String[] value();
    String message();
    String group() default "";
}
