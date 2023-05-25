package com.java.koffy.http;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KoffyResponse {

    private int status;
    private Map<String, String> headers;
    private String content;

    private KoffyResponse(Builder builder) {
        this.status = builder.status;
        this.headers = builder.headers;
        this.content = builder.content;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContent() {
        return content;
    }

    public void removeHeader(String header) {
        headers.remove(header);
    }

    public static KoffyResponse jsonResponse(int status, Map<String, String> data) {
        return new KoffyResponseFactory().response(status, "application/json",  new JSONObject(data).toString());
    }

    public static KoffyResponse textResponse(int status, String text) {
        return new KoffyResponseFactory().response(status, "text/plain",  text);
    }

    public static KoffyResponse redirectResponse(String uri) {
        return new KoffyResponseFactory().response(302, "Location", uri, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status = 200;
        private Map<String, String> headers = new HashMap<>();
        private String content = "";

        private Builder() {

        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name.toLowerCase(), value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder contentType(String contentType) {
            headers.put("Content-Type", contentType);
            return this;
        }

        public KoffyResponse build() {
            return new KoffyResponse(this);
        }
    }

    static class KoffyResponseFactory implements ResponseFactory {

        @Override
        public KoffyResponse response(int status, String contentType, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .contentType(contentType)
                    .content(content)
                    .build();
        }

        @Override
        public KoffyResponse response(int status, String name, String value, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .content(content)
                    .header(name, value)
                    .build();
        }

        @Override
        public KoffyResponse response(int status, Map<String, String> headers, String content) {
            return KoffyResponse.builder()
                    .status(status)
                    .headers(headers)
                    .content(content)
                    .build();
        }
    }
}
