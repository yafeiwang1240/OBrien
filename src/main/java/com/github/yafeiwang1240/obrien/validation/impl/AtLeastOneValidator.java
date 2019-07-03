package com.github.yafeiwang1240.obrien.validation.impl;

import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.validation.annotation.AtLeastOne;
import com.github.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AtLeastOneValidator extends AbstractValidator<AtLeastOne, Object> {

    private List<Field> fields = Lists.create(ArrayList::new);

    public AtLeastOneValidator(AtLeastOne annotation) {
        super(annotation);
    }

    @Override
    public void validate(Object o) throws ValidateFailedException {
        int fieldCount = 0;
        Class clazz = o.getClass();
        for (String fieldName : annotation.fields()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if(field.get(o) != null) {
                    fieldCount++;
                }
            } catch (NoSuchFieldException e) {
                // ignore
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
        if (fieldCount <= 0) {
            throw new ValidateFailedException(annotation.message());
        }
    }
}
