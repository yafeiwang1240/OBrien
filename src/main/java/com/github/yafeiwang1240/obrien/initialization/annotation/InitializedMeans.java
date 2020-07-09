package com.github.yafeiwang1240.obrien.initialization.annotation;

import com.github.yafeiwang1240.obrien.initialization.impl.AbstractInitializedCreate;

import java.lang.annotation.*;

@Target( ElementType.ANNOTATION_TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface InitializedMeans {
    Class<? extends AbstractInitializedCreate> value();
}
