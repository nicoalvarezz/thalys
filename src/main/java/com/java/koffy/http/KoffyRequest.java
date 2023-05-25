package com.java.koffy.http;

import java.util.Map;

public class KoffyRequest {

    private String uri;
    private HttpMethod method;
    private Map<String, String> data;
    private Map<String, String> query;

    public KoffyRequest(Builder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.data = builder.data;
        this.query = builder.query;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setPostData(Map<String, String> data) {
        this.data = data;
    }

    public void setQueryData(Map<String, String> query) {
        this.query = query;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String uri;
        private HttpMethod method;
        private Map<String, String> data;
        private Map<String, String> query;

        private Builder() {

        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder postData(Map<String, String> data) {
            this.data = data;
            return this;
        }

        public Builder queryData(Map<String, String> query) {
            this.query = query;
            return this;
        }

        public KoffyRequest build() {
            return new KoffyRequest(this);
        }
    }
}
