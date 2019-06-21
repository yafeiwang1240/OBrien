package com.githup.yafeiwang1240.obrien.validation.impl;

import com.githup.yafeiwang1240.obrien.uitls.RegexUtils;
import com.githup.yafeiwang1240.obrien.validation.annotation.Match;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;

public class MatchValidator extends AbstractValidator<Match, String> {

    public MatchValidator(Match annotation) {
        super(annotation);
    }

    @Override
    public void validate(String o) throws ValidateFailedException {
        if(o == null) {
            return;
        }
        if(!RegexUtils.matched(o, annotation.regex())) {
            throw new ValidateFailedException(annotation.message());
        }
    }
}
