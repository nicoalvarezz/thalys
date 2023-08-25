package io.thalys.exception;

/**
 * The MissingConfigException class represents an exception that is thrown when a required
 * configuration class is missing. It is a subtype of the RuntimeException class, allowing it to be
 * unchecked.
 */
public class MissingConfigException extends RuntimeException {

    private static final String MESSAGE = "Missing config class: ";

    public MissingConfigException(String missedConfig) {
        super(MESSAGE + missedConfig);
    }
}
