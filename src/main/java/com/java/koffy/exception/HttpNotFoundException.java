package com.java.koffy.exception;

public class HttpNotFoundException extends RuntimeException {

    private String message;

    public HttpNotFoundException(String message) {
        super(message);
    }
}
