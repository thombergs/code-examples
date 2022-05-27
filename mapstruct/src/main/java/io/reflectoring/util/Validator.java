package io.reflectoring.util;

import io.reflectoring.exception.ValidationException;

public class Validator {
    public int validateId(int id) throws ValidationException {
        if(id == -1){
            throw new ValidationException("Invalid ID value");
        }
        return id;
    }
}
