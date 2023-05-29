package com.java.koffy.http;

/**
 * Http Method enum class.
 */
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    /**
     * Returns the String value of a specific method.
     * @return {@link String} method value
     */
    public String get() {
        return method;
    }
}
