package com.java.koffy.http;

import com.java.koffy.http.Headers.ContentType;
import com.java.koffy.http.Headers.HttpHeader;
import com.java.koffy.http.Headers.HttpHeaders;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

/**
 * HTTP Response data structure.
 */
public final class ResponseEntity {

    /**
     * Response HTTP status code.
     */
    private HttpStatus status;

    /**
     * Response HTTP headers.
     */
    private HttpHeaders headers;

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
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Return the HTTP headers of the response.
     * @return {@link Map} HTTP headers of the response
     */
    public HttpHeaders getHttpHeaders() {
        return headers;
    }

    /**
     * Return a specific HTTP header from the response. If the header does not exist,
     * the method will return {@link Optional}
     * @param header
     * @return {@link Optional<String>}
     */
    public Optional<String> getHeader(HttpHeader header) {
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
    public void removeHeader(HttpHeader header) {
        headers.removeHeader(header);
    }

    public void setHeader(HttpHeader header, String value) {
        headers.add(header, value);
    }

    /**
     * Return a {@link ResponseEntity object with content in application/json format.
     * @param status status code of the response
     * @param content content of the response
     * @return {@link ResponseEntity object with given status, and given content in json format
     */
    public static ResponseEntity jsonResponse(Map<String, String> content, HttpStatus status) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, new JSONObject(content).toString(), status)
                .build();
    }

    public static ResponseEntity jsonResponse(Map<String, String> content, HttpStatus status, HttpHeaders headers) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, new JSONObject(content).toString(), status)
                .headers(headers)
                .build();
    }

    public static ResponseEntity jsonResponse(JSONObject content, HttpStatus status) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, content.toString(), status)
                .build();
    }

    public static ResponseEntity jsonResponse(JSONObject content, HttpStatus status, HttpHeaders headers) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, content.toString(), status)
                .headers(headers)
                .build();
    }

    public static ResponseEntity jsonResponse(String content, HttpStatus status) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, content, status)
                .build();
    }

    public static ResponseEntity jsonResponse(String content, HttpStatus status, HttpHeaders headers) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_JSON, content, status)
                .headers(headers)
                .build();
    }

    /**
     * Return a {@link ResponseEntity object with content in text/plain format.
     * @param HTTP status code of the response
     * @param text content of the response
     * @return {@link ResponseEntity } object with given status, and given data in text format
     */
    public static ResponseEntity textResponse(String text, HttpStatus status) {
        return new ResponseEntityFactory().response(ContentType.TEXT_PLAIN, text, status).build();
    }

    public static ResponseEntity textResponse(String text, HttpStatus status, HttpHeaders headers) {
        return new ResponseEntityFactory()
                .response(ContentType.TEXT_PLAIN, text, status)
                .headers(headers)
                .build();
    }

    /**
     * Return a {@link ResponseEntity object to redirect to other path.
     * @param uri path to be redirected
     * @return {@link ResponseEntity } object in the form of redirect
     */
    public static ResponseEntity redirectResponse(String uri) {
        return new Builder()
                .status(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, uri)
                .build();
    }

    public static ResponseEntity redirectResponse(String uri, HttpHeaders headers) {
        return new Builder()
                .status(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, uri)
                .headers(headers)
                .build();
    }

    public static ResponseEntity xmlResponse(String content, HttpStatus status) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_XML, content, status).build();
    }

    public static ResponseEntity xmlResponse(String content, HttpStatus status, HttpHeaders headers) {
        return new ResponseEntityFactory()
                .response(ContentType.APPLICATION_XML, content, status)
                .headers(headers)
                .build();
    }

    public static ResponseEntity response(String content, ContentType contentType,
                                          HttpHeaders headers, HttpStatus status) {
        return new ResponseEntityFactory()
                .response(contentType, content, status)
                .headers(headers)
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
        private HttpStatus status = HttpStatus.OK;

        /**
         * Response HTTP headers.
         */
        private HttpHeaders headers = new HttpHeaders();

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
        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Set a single header and its value of the response for the new instance of {@link ResponseEntity}.
         * @param header header
         * @param value header value
         * @return Builder of {@link ResponseEntity} instance
         */
        public Builder header(HttpHeader header, String value) {
            this.headers.add(header, value);
            return this;
        }


        /**
         * Set all the headers that the response will contain in one go for the new instance of {@link ResponseEntity}.
         * @param headers Map containing all the headers and their respective values for the response
         * @return Builder of {@link ResponseEntity} instance
         */
        public Builder headers(HttpHeaders headers) {
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
        public Builder contentType(ContentType contentType) {
            headers.add(HttpHeader.CONTENT_TYPE, contentType.get());
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
        public Builder response(ContentType contentType, String content, HttpStatus status) {
            return ResponseEntity.builder()
                    .contentType(contentType)
                    .status(status)
                    .content(content);
        }
    }
}
