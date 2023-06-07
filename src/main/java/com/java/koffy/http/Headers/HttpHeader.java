package com.java.koffy.http.Headers;

/**
 * HTTP Header enum.
 * It allows to access already existing headers as constants
 * This approach offers the advantage of type safety and can help prevent errors when referencing header names.
 */
public enum HttpHeader {

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

    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    /**
     * Get header name.
     * @return {@link String}
     */
    public String get() {
        return headerName;
    }

    /**
     * Get HttpHeader from a given string.
     * @param header {@link String}
     * @return {@link HttpHeader}
     * @throws IllegalArgumentException
     */
    public HttpHeader headerOf(String header) {
        HttpHeader currentHeader = resolve(header);
        if (currentHeader == null) {
            throw new IllegalArgumentException("No matching content for header:" + header);
        }
        return currentHeader;
    }

    private static HttpHeader resolve(String header) {
        HttpHeader[] headers = values();

        for (HttpHeader currentHeader : headers) {
            if (currentHeader.headerName.equals(header)) {
                return currentHeader;
            }
        }
        return null;
    }
}
