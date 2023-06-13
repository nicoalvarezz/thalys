package com.java.koffy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.exception.ConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

public class Validator {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    private final static javax.validation.Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    public static <T> T validate(Map<String, String> value, Class<?> clazz) {
        T instance = MAPPER.convertValue(value, MAPPER.getTypeFactory().constructType(clazz));

        if (instance == null) {
            throw new NullPointerException("Unable to validate null object");
        }

        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(instance);

        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new ConstraintViolationException(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return instance;
    }
}
