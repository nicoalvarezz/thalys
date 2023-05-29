package com.java.koffy.http;

/**
 * HTTP Enum header class.
 */
enum Header {

    LOCATION("Location"),
    CONTENT_TYPE("Content-Type");

    private final String header;

    Header(String header) {
        this.header = header;
    }

    /**
     * Returns the String value of a specific header
     * @return {@link String} header value
     */
    public String get() {
        return header;
    }
}
