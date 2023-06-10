package com.java.koffy.http;

/**
 * HTTP Content-Type.
 */
public enum ContentType {

    JSON("application/json"),
    TEXT("text/plain"),
    HTML("text/html"),
    XML("application/xml"),
    JPEG("image/jpeg"),
    PDF("application/pdf"),
    URL_ENCODED("application/x-www-form-urlencoded"),
    OCTET_STREAM("application/octet-stream"),
    FORM_DATA("multipart/form-data"),
    JAVASCRIPT("application/javascript");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns the String value of a specific content type.
     * @return {@link String} content type value
     */
    public String get() {
        return contentType;
    }

    public static ContentType contentTypeOf(String contentType) {
        ContentType currentContentType = resolve(contentType);

        if (currentContentType == null) {
            throw new IllegalArgumentException("No matching content for header:" + contentType);
        }
        return currentContentType;
    }

    private static ContentType resolve(String contentType) {
        ContentType[] contentTypes = values();

        for (ContentType currentContentType : contentTypes) {
            if (currentContentType.get().equals(contentType)) {
                return currentContentType;
            }
        }
        return null;
    }
}
