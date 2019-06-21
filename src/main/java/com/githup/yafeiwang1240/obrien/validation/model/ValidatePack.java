package com.githup.yafeiwang1240.obrien.validation.model;

import com.githup.yafeiwang1240.obrien.lang.Lists;
import com.githup.yafeiwang1240.obrien.lang.Maps;
import com.githup.yafeiwang1240.obrien.validation.exception.ValidateFailedException;
import com.githup.yafeiwang1240.obrien.validation.impl.AbstractValidator;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatePack {
    private final Map<String, List<FieldValidateExecutor>> fieldValidators = Maps.create(HashMap::new);
    private final Map<String, List<AbstractValidator>> classValidators = Maps.create(HashMap::new);

    public void addFieldValidator(Field field, AbstractValidator validator) {
        addFieldValidator(field, "", validator);
    }

    public void addFieldValidator(Field field, String group, AbstractValidator validator) {
        FieldValidateExecutor executor = new FieldValidateExecutor(field, validator);
        if(fieldValidators.containsKey(group)) {
            fieldValidators.get(group).add(executor);
        } else {
            fieldValidators.put(group, Arrays.asList(executor));
        }
    }

    public void addClassValidator(AbstractValidator validator) {
        addClassValidator("", validator);
    }

    public void addClassValidator(String group, AbstractValidator validator) {
        if(classValidators.containsKey(group)) {
            classValidators.get(group).add(validator);
        } else {
            classValidators.put(group, Arrays.asList(validator));
        }
    }

    public ValidateResult validate(Object o) {
        return validate("", o);
    }

    public ValidateResult validate(String group, Object o) {
        ValidateResult result = new ValidateResult();
        result.setStatus(ValidateResult.Status.SUCCESS);
        List<String> messages = Lists.create(ArrayList::new);
        List<AbstractValidator> validators = classValidators.get(group);
        if (validators != null && validators.size() > 0) {
            validators.forEach(_value -> {
                try {
                    _value.validate(o);
                } catch (ValidateFailedException e) {
                    messages.add(e.getMessage());
                }
            });
        }

        List<FieldValidateExecutor> executors = fieldValidators.get(group);
        if(executors != null && executors.size() > 0) {
            executors.forEach(_value -> {
                try {
                    _value.process(o);
                } catch (ValidateFailedException e) {
                    messages.add(e.getMessage());
                }
            });
        }
        if(messages.isEmpty()) return result;
        result.setStatus(ValidateResult.Status.FAILED);
        result.setMessages(messages);
        return result;
    }
}
