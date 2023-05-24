package com.java.koffy.http;

import java.util.Map;

public class KoffyRequest {

    private String uri;
    private HttpMethod method;
    private Map<String, String> data;
    private Map<String, String> query;

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
}
