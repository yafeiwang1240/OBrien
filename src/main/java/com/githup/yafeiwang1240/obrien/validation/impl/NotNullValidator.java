package com.githup.yafeiwang1240.obrien.validation.impl;

import com.githup.yafeiwang1240.obrien.validation.annotation.NotNull;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

public class NotNullValidator extends AbstractValidator<NotNull, Object> {

    public NotNullValidator(NotNull annotation) {
        super(annotation);
    }

    @Override
    public void validate(Object o) throws ValidateFailedException {
        if (o == null) {
            throw new ValidateFailedException(annotation.message());
        }
    }
}
