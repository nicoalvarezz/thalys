package com.java.koffy.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Response {

    private int status = 200;

    private Map<String, String> headers = new HashMap<>();

    private Optional<String> content = Optional.empty();

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContent() {
        return content.orElse("");
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHeader(String header, String value) {
        this.headers.put(header.toLowerCase(), value);
    }

    public void removeHeader(String header) {
        headers.remove(header);
    }

    public void setContent(String content) {
        this.content = Optional.of(content);
    }
}
