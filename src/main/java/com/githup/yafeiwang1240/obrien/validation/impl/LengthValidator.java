package com.githup.yafeiwang1240.obrien.validation.impl;

import com.githup.yafeiwang1240.obrien.validation.annotation.Length;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

public class LengthValidator extends AbstractValidator<Length, String> {

    public LengthValidator(Length annotation) {
        super(annotation);
    }

    @Override
    public void validate(String o) throws ValidateFailedException {
        if(o == null) {
            if(annotation.min() == 0) return;
            throw new ValidateFailedException(annotation.message());
        }
        if(o.length() < annotation.min() || o.length() > annotation.max()) {
            throw new ValidateFailedException(annotation.message());
        }
    }
}
