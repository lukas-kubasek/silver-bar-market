package com.cs.orderboard.model.validate;

public interface Validator<T> {

    boolean isValid(T entity);

    void validate(T entity);
}
