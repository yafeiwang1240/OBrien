package com.github.yafeiwang1240.obrien.validation;

public interface IValidator<T> {
    boolean isValid(T object);
    String massage();
}
