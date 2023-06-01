package com.java.koffy.http;

/**
 * HTTP Enum header class.
 */
public enum Header {

    LOCATION("location"),
    CONTENT_TYPE("content-type"),
    AUTHORIZATION("authorization"),
    ACCEPT("accept"),
    USER_AGENT("user-agent"),
    HOST("host"),
    CACHE_CONTROL("cache-control"),
    CONTENT_LENGTH("content-length"),
    IF_MODIFIED_SINCE("if-modified-since"),
    SERVER("server"),
    ETAG("etag");

    private final String header;

    Header(String header) {
        this.header = header;
    }

    /**
     * Returns the String value of a specific header.
     * @return {@link String} header value
     */
    public String get() {
        return header;
    }
}
