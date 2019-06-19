package com.githup.yafeiwang1240.obrien.validation;

public interface IValidator<T> {
    boolean isValid(T object);
    String massage();
}
