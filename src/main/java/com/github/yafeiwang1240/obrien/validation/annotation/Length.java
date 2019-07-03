package com.github.yafeiwang1240.obrien.validation.annotation;

import com.github.yafeiwang1240.obrien.validation.impl.LengthValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(LengthValidator.class)
public @interface Length {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message();
    String group() default  "";
}
