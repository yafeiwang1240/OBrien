package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public abstract class AbstractInitializedCreate<A extends Annotation> {

    protected A annotation;
    public AbstractInitializedCreate(A annotation) {
        this.annotation = annotation;
    }

    public abstract Map<String, List<Initialized>> getInitExecutor() throws InitializedFailedException;
}
