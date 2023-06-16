package com.java.koffy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

public class Validator {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    private final static javax.validation.Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    /**
     * Method used to validate request bodies or params by serialising their validatable classes.
     * These classes contain the annotations and constraints that will be thrown during the serialisation if broken.
     * The constraint violations is used to gather all the exceptions and throw an error.
     * If no violation happened the serialised object is returned.
     * @param value data to be serialised
     * @param clazz validatable class
     * @return serialized instance
     * @throws {@link ConstraintViolationException}
     */
    public static <T> T validate(Map<String, String> value, Class<?> clazz) {
        T instance = MAPPER.convertValue(value, MAPPER.getTypeFactory().constructType(clazz));

        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(instance);

        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new ConstraintViolationException(validationMessage(
                    violation.getPropertyPath().toString(), violation.getMessage()), violations);
        }
        return instance;
    }

    private static String validationMessage(String field, String violationMessage) {
        return "Validation failed for field '" + field + "': " + violationMessage;
    }
}
