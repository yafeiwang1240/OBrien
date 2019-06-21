package com.githup.yafeiwang1240.obrien.validation.impl;

import com.githup.yafeiwang1240.obrien.lang.Sets;
import com.githup.yafeiwang1240.obrien.validation.annotation.StringIn;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

import java.util.Set;

public class StringInValidator extends AbstractValidator<StringIn, String> {

    Set<String> values;

    public StringInValidator(StringIn annotation) {
        super(annotation);
        values = Sets.newHashSet(annotation.value().length);
        for (String value : annotation.value()) {
            values.add(value);
        }
    }

    @Override
    public void validate(String o) throws ValidateFailedException {
        if(!values.contains(o)) {
            throw new ValidateFailedException(annotation.message());
        }
    }
}
