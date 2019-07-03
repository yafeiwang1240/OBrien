package com.github.yafeiwang1240.obrien.validation.annotation;

import com.github.yafeiwang1240.obrien.validation.impl.AbstractValidator;

import java.lang.annotation.*;

@Target( ElementType.ANNOTATION_TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface ValidateRule {
    Class<? extends AbstractValidator> value();
}
