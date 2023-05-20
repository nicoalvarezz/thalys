package com.java.koffy.http;

public class HttpNotFoundException extends RuntimeException {

    private String message;

    public HttpNotFoundException(String message) {
        super(message);
    }
}
