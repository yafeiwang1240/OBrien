package com.githup.yafeiwang1240.obrien.validation.impl;

import com.githup.yafeiwang1240.obrien.lang.Lists;
import com.githup.yafeiwang1240.obrien.validation.IValidator;
import com.githup.yafeiwang1240.obrien.validation.annotation.UserDefined;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

import java.util.List;

public class UserDefinedValidator extends AbstractValidator<UserDefined, Object> {

    private List<IValidator> validators;

    public UserDefinedValidator(UserDefined annotation) throws IllegalAccessException, InstantiationException {
        super(annotation);
        validators = Lists.newArrayList(annotation.validators().length);
        for(Class<? extends IValidator> clazz : annotation.validators()) {
            validators.add(clazz.newInstance());
        }
    }

    @Override
    public void validate(Object o) throws ValidateFailedException {
        StringBuilder builder = new StringBuilder();
        validators.forEach(_value -> {
            if(!_value.isValid(o)) {
                builder.append(_value.massage()).append("\n");
            }
        });
        if (builder.length() > 0) {
            throw new ValidateFailedException(builder.toString());
        }
    }
}
