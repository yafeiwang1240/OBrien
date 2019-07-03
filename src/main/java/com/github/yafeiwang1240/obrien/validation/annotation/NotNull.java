package com.github.yafeiwang1240.obrien.validation.annotation;

import com.github.yafeiwang1240.obrien.validation.impl.NotNullValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(NotNullValidator.class)
public @interface NotNull {
    String message();
    String group() default "";
}
