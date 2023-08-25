package io.thalys.exception;

public class MissingApplicationProperty extends RuntimeException {

    private static final String MESSAGE = "Missing or invalid application property: ";

    public MissingApplicationProperty(String missedProperty) {
        super(MESSAGE + missedProperty);
    }
}
