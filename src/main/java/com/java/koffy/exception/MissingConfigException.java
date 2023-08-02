package com.java.koffy.exception;

public class MissingConfigException extends RuntimeException {

    private static final String MESSAGE = "Missing config class: ";

    public MissingConfigException(String missedConfig) {
        super(MESSAGE + missedConfig);
    }
}
