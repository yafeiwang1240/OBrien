package com.githup.yafeiwang1240.obrien.validation.annotation;

import com.githup.yafeiwang1240.obrien.validation.impl.NotNullValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(NotNullValidator.class)
public @interface NotNull {
    String message();
    String group() default "";
}
