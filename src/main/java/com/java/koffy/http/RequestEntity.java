package com.java.koffy.http;

import com.java.koffy.App;
import com.java.koffy.container.Container;
import com.java.koffy.http.Headers.HttpHeaders;
import com.java.koffy.routing.Route;

import java.util.HashMap;
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
     * HTTP request route.
     */
    private Optional<Route> route;

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

    private Object serialized;

    private RequestEntity(Builder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.data = builder.data;
        this.query = builder.query;
        this.headers = builder.headers;
        this.route = Container.resolve(App.class).router().resolveRoute(uri, method);
    }

    /**
     * Retrieve the URI of the request.
     * @return {@link String} URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Retrieve the route of the request.
     * @return {@link Optional<Route>}
     */
    public Optional<Route> getRoute() {
        return route;
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
        return headers.getHeaders();
    }

    /**
     * Retrieve header value of specific header of the request.
     * @param header {@link HttpHeaders}
     * @return {@link Optional<String>}
     */
    public Optional<String> getHeader(String header) {
        return Optional.ofNullable(headers.get(header));
    }

    /**
     * Retrieve route parameters of the request.
     * @return {@link Map} with the route parameters
     */
    public Map<String, String> routeParams() {
        if (route.isPresent()) {
            return route.get().parseParameter(uri);
        }
        return new HashMap<>();
    }

    public void setSerialized(Object dto) {
        this.serialized = dto;
    }

    public <T> T getSerialized(Class<T> type) {
       return type.cast(serialized);
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

        /**
         * Creates a new instance of {@link RequestEntity}.
         * @return The new instance
         */
        public RequestEntity build() {
            return new RequestEntity(this);
        }
    }
}
