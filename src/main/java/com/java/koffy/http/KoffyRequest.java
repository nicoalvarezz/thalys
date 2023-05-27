package com.java.koffy.http;

import java.util.Map;

/**
 * HTTP Request data structure that is received from the user request.
 */
public class KoffyRequest {

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

    public KoffyRequest(Builder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.data = builder.data;
        this.query = builder.query;
    }

    /**
     * Retrieve the URI of the request.
     * @return String URI
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
     * @return post data
     */
    public Map<String, String> getPostData() {
        return data;
    }

    /**
     * Retrieve the query data of the request.
     * @return query data
     */
    public Map<String, String> getQueryData() {
        return query;
    }

    /**
     * Create instance of {@link Builder}.
     * @return The new instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new instance of {@link KoffyRequest}.
     * This class is used to implement the builder pattern of {@link KoffyRequest}
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

        private Builder() {

        }

        /**
         * Set the URI of the request of the new instance {@link KoffyRequest}.
         * @param uri URI of the request
         * @return Builder of {@link KoffyRequest} instance
         */
        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Set the method of the request of the new instance {@link KoffyRequest}.
         * @param method HTTP method {@link HttpMethod}
         * @return Builder of {@link KoffyRequest} instance
         */
        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * Set the post data of new instance {@link KoffyRequest}.
         * @param data post data of the request
         * @return Builder of {@link KoffyRequest} instance
         */
        public Builder postData(Map<String, String> data) {
            this.data = data;
            return this;
        }

        /**
         * Set the query of the new instance {@link KoffyRequest}.
         * @param query query data of the request
         * @return Builder of {@link KoffyRequest} instance
         */
        public Builder queryData(Map<String, String> query) {
            this.query = query;
            return this;
        }

        /**
         * Creates a new instance of {@link KoffyRequest}.
         * @return The new instance
         */
        public KoffyRequest build() {
            return new KoffyRequest(this);
        }
    }
}
