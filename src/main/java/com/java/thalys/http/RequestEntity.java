package com.java.thalys.http;

import com.java.thalys.http.Headers.HttpHeader;
import com.java.thalys.http.Headers.HttpHeaders;
import com.java.thalys.routing.Route;

import java.util.Map;
import java.util.Optional;

/**
 * HTTP Request data structure.
 */
public final class RequestEntity {

    /**
     * HTTP request URI.
     */
    private String uri;

    /**
     * HTTP request method.
     */
    private HttpMethod method;

    /**
     * HTTP request post data.
     */
    private Map<String, String> data;

    /**
     * HTTP request query data.
     */
    private Map<String, String> query;

    /**
     * HTTP request headers.
     */
    private HttpHeaders headers;

    /**
     * Serialised objected from validation.
     */
    private Object serialized;

    private Route route;

    private RequestEntity(Builder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.data = builder.data;
        this.query = builder.query;
        this.headers = builder.headers;
        this.route = builder.route;
    }

    /**
     * Retrieve the URI of the request.
     * @return {@link String} URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Retrieve the HTTP method of the request.
     * @return {@link HttpMethod}
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * Retrieve the post data of the request.
     * @return {@link Map} post data
     */
    public Map<String, String> getPostData() {
        return data;
    }

    /**
     * Retrieve post data value of specific key of the request.
     * @param key {@link String} post data key
     * @return {@link Optional<String>} post value
     */
    public Optional<String> getPostData(String key) {
        return Optional.ofNullable(data.get(key));
    }

    /**
     * Retrieve the query data of the request.
     * @return {@link Map} query data
     */
    public Map<String, String> getQueryData() {
        return query;
    }

    /**
     * Retrieve query data value of specific key of the request.
     * @param key {@link String} query data key
     * @return {@link Optional<String>) query value
     */
    public Optional<String> getQueryData(String key) {
        return Optional.ofNullable(query.get(key));
    }

    /**
     * Retrieve the headers of the request.
     * @return {@link Map} headers
     */
    public Map<String, String> getHeaders() {
        return headers.headers();
    }

    /**
     * Retrieve header value of specific header of the request.
     * @param header {@link HttpHeaders}
     * @return {@link Optional<String>}
     */
    public Optional<String> getHeader(HttpHeader header) {
        return Optional.ofNullable(headers.get(header));
    }

    /**
     * Retrieve route parameters of the request.
     * @return {@link Map} with the route parameters
     */
    public Map<String, String> routeParams() {
        return route.parseParameter(uri);
    }

    public Route getRoute() {
        return route;
    }

    /**
     * Create instance of {@link Builder}.
     * @return {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new instance of {@link RequestEntity}.
     * This class is used to implement the builder pattern of {@link RequestEntity}
     */
    public static final class Builder {

        /**
         * HTTP request URI.
         */
        private String uri;

        /**
         * HTTP request method.
         */
        private HttpMethod method;

        /**
         * HTTP request post data.
         */
        private Map<String, String> data;

        /**
         * HTTP request query data.
         */
        private Map<String, String> query;

        /**
         * HTTP headers.
         */
        private HttpHeaders headers;

        /**
         * HTTP route.
         */
        private Route route;

        private Builder() {

        }

        /**
         * Set the URI of the request of the new instance {@link RequestEntity}.
         * @param uri URI of the request
         * @return Builder of {@link RequestEntity} instance
         */
        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Set the method of the request of the new instance {@link RequestEntity}.
         * @param method HTTP method {@link HttpMethod}
         * @return Builder of {@link RequestEntity} instance
         */
        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * Set the post data of new instance {@link RequestEntity}.
         * @param data post data of the request
         * @return Builder of {@link RequestEntity} instance
         */
        public Builder postData(Map<String, String> data) {
            this.data = data;
            return this;
        }

        /**
         * Set the query of the new instance {@link RequestEntity}.
         * @param query query data of the request
         * @return Builder of {@link RequestEntity} instance
         */
        public Builder queryData(Map<String, String> query) {
            this.query = query;
            return this;
        }

        /**
         * Set headers of the new instance {@link RequestEntity}.
         * @param headers {@link Map} containing request headers
         * @return Builder of {@link RequestEntity} instance
         */
        public Builder headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder route(Route route) {
            this.route = route;
            return this;
        }

        /**
         * Creates a new instance of {@link RequestEntity}.
         * @return The new instance
         */
        public RequestEntity build() {
            return new RequestEntity(this);
        }
    }
}
