package com.github.yafeiwang1240.obrien.validation.impl;

import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.validation.annotation.ExactOne;
import com.github.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExactOneValidator extends AbstractValidator<ExactOne, Object> {

    private List<Field> fields = Lists.create(ArrayList::new);

    public ExactOneValidator(ExactOne annotation) {
        super(annotation);
    }

    @Override
    public void validate(Object o) throws ValidateFailedException {
        Class clazz = o.getClass();
        int fieldCount = 0;
        for (String fieldName : annotation.fields()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if(field.get(o) != null) fieldCount++;
            } catch (NoSuchFieldException e) {
                // ignore
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
        if (fieldCount != 1) throw new ValidateFailedException(annotation.message());
    }
}
