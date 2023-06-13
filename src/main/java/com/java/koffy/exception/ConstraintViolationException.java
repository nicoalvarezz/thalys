package com.java.koffy.exception;

public class ConstraintViolationException extends RuntimeException {

    private final String field;

    private final String message;

    public ConstraintViolationException(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return "Validation failed for field '" + field + "': " + message;
    }

    public String getField() {
        return field;
    }
}
