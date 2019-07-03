package com.github.yafeiwang1240.obrien.validation.annotation;

import com.github.yafeiwang1240.obrien.validation.IValidator;
import com.github.yafeiwang1240.obrien.validation.impl.UserDefinedValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ValidateRule(UserDefinedValidator.class)
public @interface UserDefined {
    Class<? extends IValidator>[] validators();
    String group() default "";
}
