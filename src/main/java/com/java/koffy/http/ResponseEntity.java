package com.java.koffy.http;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP Response data structure.
 */
public final class ResponseEntity {

    /**
     * Response HTTP status code.
     */
    private int status;

    /**
     * Response HTTP headers.
     */
    private Map<Header, String> headers;

    /**
     * Response content.
     */
    private String content;

    private ResponseEntity(Builder builder) {
        this.status = builder.status;
        this.headers = builder.headers;
        this.content = builder.content;
    }

    /**
     * Return the HTTP status code of the response.
     * @return HTTP status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Return the HTTP headers of the response.
     * @return {@link Map} HTTP headers of the response
     */
    public Map<Header, String> getHeaders() {
        return headers;
    }

    /**
     * Return a specific HTTP header from the response. If the header does not exist,
     * the method will return {@link Optional}
     * @param header
     * @return {@link Optional<String>}
     */
    public Optional<String> getHeader(Header header) {
        return Optional.ofNullable(headers.get(header));
    }

    /**
     * Return the content of the response.
     * @return {@link String} content in the response
     */
    public String getContent() {
        return content;
    }

    /**
     * Delete a specific header form the response.
     * @param header header name to be removed
     */
    public void removeHeader(Header header) {
        headers.remove(header);
    }

    public void setHeader(Header header, String value) {
        headers.put(header, value);
    }

    /**
     * Return a {@link ResponseEntity object with content in application/json format.
     * @param status status code of the response
     * @param content content of the response
     * @return {@link ResponseEntity object with given status, and given content in json format
     */
    public static Builder jsonResponse(Map<String, String> content) {
        return new ResponseEntityFactory().response(ContentType.JSON.get(), new JSONObject(content).toString());
    }

    /**
     * Return a {@link ResponseEntity object with content in text/plain format.
     * @param HTTP status code of the response
     * @param text content of the response
     * @return {@link ResponseEntity } object with given status, and given data in text format
     */
    public static Builder textResponse(String text) {
        return new ResponseEntityFactory().response(ContentType.TEXT.get(), text);
    }

    /**
     * Return a {@link ResponseEntity object to redirect to other path.
     * @param uri path to be redirected
     * @return {@link ResponseEntity } object in the form of redirect
     */
    public static ResponseEntity redirectResponse(String uri) {
        return new ResponseEntityFactory()
                .response()
                .status(302)
                .header(Header.LOCATION, uri)
                .build();
    }

    /**
     * Creates instance of {@link Builder}.
     * @return The new instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new instance of {@link ResponseEntity}.
     * This class is used to implement the builder pattern of {@link ResponseEntity}
     */
    public static final class Builder {

        /**
         * Response HTTP status code.
         */
        private int status = 200;

        /**
         * Response HTTP headers.
         */
        private Map<Header, String> headers = new HashMap<>();

        /**
         * Response content.
         */
        private String content = "";

        private Builder() {

        }

        /**
         * Set the status code of the response of the new instance {@link ResponseEntity}.
         * @param status status code of the response
         * @return Builder
         */
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Set a single header and its value of the response for the new instance of {@link ResponseEntity}.
         * @param name header name
         * @param value header value
         * @return Builder of {@link ResponseEntity} instance
         */
        public Builder header(Header name, String value) {
            this.headers.put(name, value);
            return this;
        }


        /**
         * Set all the headers that the response will contain in one go for the new instance of {@link ResponseEntity}.
         * @param headers Map containing all the headers and their respective values for the response
         * @return Builder of {@link ResponseEntity} instance
         */
        public Builder headers(Map<Header, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Set the content of the response for the new instance of {@link ResponseEntity}.
         * @param content content of the response
         * @return Builder of {@link ResponseEntity} instance
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Set the content type header specifically for the new instance of {@link ResponseEntity}.
         * @param contentType value of the content type header
         * @return Builder of the {@link ResponseEntity} instance
         */
        public Builder contentType(String contentType) {
            headers.put(Header.CONTENT_TYPE, contentType);
            return this;
        }

        /**
         * Creates a new instance of {@link ResponseEntity}.
         * @return The new instance
         */
        public ResponseEntity build() {
            return new ResponseEntity(this);
        }
    }

    /**
     * This class implements {@link ResponseFactory} interface to implement the different types of responses.
     * This class is used to implement the factory pattern
     */
    static class ResponseEntityFactory implements ResponseFactory {
        /**
         * Response method that takes the content-type and content.
         * Returns response builder with given content-type and content.
         * @param contentType value of the content type header
         * @param content content of the response
         * @return {@link Builder}
         */
        @Override
        public Builder response(String contentType, String content) {
            return ResponseEntity.builder()
                    .contentType(contentType)
                    .content(content);
        }

        /**
         * Response method that returns empty response builder.
         * @return {@link Builder}
         */
        @Override
        public Builder response() {
            return ResponseEntity.builder();
        }
    }
}
