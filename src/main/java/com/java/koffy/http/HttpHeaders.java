package com.java.koffy.http;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpHeaders {

    public static final String LOCATION = "location";

    public static final String CONTENT_TYPE = "content-type";

    public static final String AUTHORIZATION = "authorization";

    public static final String ACCEPT = "accept";

    public static final String USER_AGENT = "user-agent";

    public static final String HOST = "host";

    public static final String CACHE_CONTROL = "cache-control";

    public static final String CONTENT_LENGTH = "content-length";

    public static final String IF_MODIFIED_SINCE = "if-modified-since";

    public static final String SERVER = "server";

    public static final String ETAG = "etag";

    private final Map<String, String> headers;

    public HttpHeaders() {
        headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void add(String header, String value) {
        headers.put(header, value);
    }

    public void addAll(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public String get(String header) {
        return headers.get(header);
    }

    public boolean containsHeader(String header) {
        return headers.containsKey(header);
    }

    public void removeHeader(String header) {
        headers.remove(header);
    }

    public Set<String> getAllHeaderNames() {
        return headers.keySet();
    }

    public int size() {
        return headers.size();
    }

    public boolean isEmpty() {
        return headers.isEmpty();
    }
}
