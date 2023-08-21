package com.java.thalys.exception;

public class MissingApplicationProperty extends RuntimeException {

    private static final String MESSAGE = "Missing application property: ";

    public MissingApplicationProperty(String missedProperty) {
        super(MESSAGE + missedProperty);
    }
}
