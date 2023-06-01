package com.java.koffy.http;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Response data structure that is sent to the client.
 */
public final class KoffyResponse {

    /**
     * Response HTTP status code.
     */
    private int status;

    /**
     * Response HTTP headers.
     */
    private Map<String, String> headers;

    /**
     * Response content.
     */
    private String content;

    private KoffyResponse(Builder builder) {
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
    public Map<String, String> getHeaders() {
        return headers;
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
    public void removeHeader(String header) {
        headers.remove(header);
    }

    /**
     * Return a {@link KoffyResponse object with content in application/json format.
     * @param status status code of the response
     * @param content content of the response
     * @return {@link KoffyResponse object with given status, and given content in json format
     */
    public static KoffyResponse jsonResponse(int status, Map<String, String> content) {
        return new KoffyResponseFactory().response(status, ContentType.JSON.get(),  new JSONObject(content).toString());
    }

    /**
     * Return a {@link KoffyResponse object with content in text/plain format.
     * @param HTTP status code of the response
     * @param text content of the response
     * @return {@link KoffyResponse} object with given status, and given data in text format
     */
    public static KoffyResponse textResponse(int status, String text) {
        return new KoffyResponseFactory().response(status, ContentType.TEXT.get(), text);
    }

    /**
     * Return a {@link KoffyResponse object to redirect to other path.
     * @param uri path to be redirected
     * @return {@link KoffyResponse} object in the form of redirect
     */
    public static KoffyResponse redirectResponse(String uri) {
        return new KoffyResponseFactory().response(302, Header.LOCATION.get(), uri, null);
    }

    /**
     * Return a {@link KoffyResponse} object with multiple headers.
     * @param status HTTP status code
     * @param headers {@link Map} Response headers
     * @param content Response content
     * @return {@link KoffyResponse} object with given status,given headers and content
     */
    public static KoffyResponse textResponseWithMultipleHeaders(int status,
                                                                Map<String, String> headers, String content) {
        return new KoffyResponseFactory().response(status, ContentType.TEXT.get(), headers, content);
    }

    /**
     * Return a {@link KoffyResponse} object with multiple headers in json format.
     * @param status HTTP status code
     * @param headers {@link Map} Response headers
     * @param content Response content
     * @return {@link KoffyResponse} object with given status,given headers and content
     */
    public static KoffyResponse jsonResponseWithMultipleHeaders(int status,
                                        Map<String, String> headers, Map<String, String> content) {
        return new KoffyResponseFactory().response(status,
                ContentType.JSON.get(), headers,  new JSONObject(content).toString());
    }

    /**
     * Creates instance of {@link Builder}.
     * @return The new instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new instance of {@link KoffyResponse}.
     * This class is used to implement the builder pattern of {@link KoffyResponse}
     */
    public static final class Builder {

        /**
         * Response HTTP status code.
         */
        private int status = 200;

        /**
         * Response HTTP headers.
         */
        private Map<String, String> headers = new HashMap<>();

        /**
         * Response content.
         */
        private String content = "";

        private Builder() {

        }

        /**
         * Set the status code of the response of the new instance {@link KoffyResponse}.
         * @param status status code of the response
         * @return Builder
         */
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Set a single header and its value of the response for the new instance of {@link KoffyResponse}.
         * @param name header name
         * @param value header value
         * @return Builder of {@link KoffyResponse} instance
         */
        public Builder header(String name, String value) {
            this.headers.put(name.toLowerCase(), value);
            return this;
        }


        /**
         * Set all the headers that the response will contain in one go for the new instance of {@link KoffyResponse}.
         * @param headers Map containing all the headers and their respective values for the response
         * @return Builder of {@link KoffyResponse} instance
         */
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Set the content of the response for the new instance of {@link KoffyResponse}.
         * @param content content of the response
         * @return Builder of {@link KoffyResponse} instance
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Set the content type header specifically for the new instance of {@link KoffyResponse}.
         * @param contentType value of the content type header
         * @return Builder of the {@link KoffyResponse} instance
         */
        public Builder contentType(String contentType) {
            headers.put(Header.CONTENT_TYPE.get(), contentType);
            return this;
        }

        /**
         * Creates a new instance of {@link KoffyResponse}.
         * @return The new instance
         */
        public KoffyResponse build() {
            return new KoffyResponse(this);
        }
    }

    /**
     * This class implements {@link ResponseFactory} interface to implement the different types of responses.
     * This class is used to implement the factory pattern
     */
    static class KoffyResponseFactory implements ResponseFactory {

        // TODO: Refactor response factory implementation using setters instead of current implementation
        /**
         * Response method that takes the status code, content and only the content-type header.
         * @param status status code of the response
         * @param contentType value of the content type header
         * @param content content of the response
         * @return {@link KoffyResponse}
         */
        @Override
        public KoffyResponse response(int status, String contentType, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .contentType(contentType)
                    .content(content)
                    .build();
        }

        /**
         * Response method that takes the status code, content, and a single header adn its value.
         * @param status status code of the response
         * @param name header name
         * @param value header value
         * @param content content of the response
         * @return {@link KoffyResponse}
         */
        @Override
        public KoffyResponse response(int status, String name, String value, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .content(content)
                    .header(name, value)
                    .build();
        }

        /**
         * Response method that takes the status code, the content,
         * and all the headers required for the response in one go.
         * @param status status code of the response
         * @param headers Map containing all the headers and their respective values for the response
         * @param content content of the response
         * @return {@link KoffyResponse}
         */
        @Override
        public KoffyResponse response(int status, String contentType, Map<String, String> headers, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .headers(headers)
                    .contentType(contentType)
                    .content(content)
                    .build();
        }
    }
}
