package com.github.yafeiwang1240.obrien.validation.model;

import com.github.yafeiwang1240.obrien.validation.exception.ValidateFailedException;
import com.github.yafeiwang1240.obrien.validation.impl.AbstractValidator;

import java.lang.reflect.Field;

public class FieldValidateExecutor {
    private Field field;
    private AbstractValidator validator;

    public FieldValidateExecutor(Field field, AbstractValidator validator) {
        this.field = field;
        this.field.setAccessible(true);
        this.validator = validator;
    }

    public void process(Object o) throws ValidateFailedException {
        try {
            validator.validate(field.get(o));
        } catch (IllegalAccessException e) {
            // ignore
        }
    }
}
