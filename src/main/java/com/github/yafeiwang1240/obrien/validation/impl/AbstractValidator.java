package com.github.yafeiwang1240.obrien.validation.impl;

import com.github.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

import java.lang.annotation.Annotation;

public abstract class AbstractValidator<A extends Annotation, T> {
    protected A annotation;
    public AbstractValidator(A annotation) {
        this.annotation = annotation;
    }

    public abstract void validate(T o) throws ValidateFailedException;
}
