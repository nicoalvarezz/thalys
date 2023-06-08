package com.java.koffy.http.Headers;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HttP Headers class.
 * Data structure to store headers and its values.
 */
public class HttpHeaders {

    private final Map<String, String> headers;

    public HttpHeaders() {
        headers = new HashMap<>();
    }

    /**
     * Return all headers.
     * @return {@link Map}
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Add header and value to the headers {@link Map}.
     * @param header header name
     * @param value header value
     */
    public void add(String header, String value) {
        headers.put(header, value);
    }

    /**
     * Add various headers and their values at once.
     * @param headers {@link Map}
     */
    public void addAll(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    /**
     * Return the value of a specific header.
     * @param header header name
     * @return {@link String} header value
     */
    public String get(String header) {
        return headers.get(header);
    }

    /**
     * Check if a header name exits.
     * @param header header name
     * @return {@link Boolean}
     */
    public boolean containsHeader(String header) {
        return headers.containsKey(header);
    }

    /**
     * Remove a header.
     * @param header header name
     */
    public void removeHeader(String header) {
        headers.remove(header);
    }

    /**
     * Return all the names of the headers.
     * @return {@link Set} header names
     */
    public Set<String> getAllHeaderNames() {
        return headers.keySet();
    }

    /**
     * Get the number of headers.
     * @return {@link Integer}
     */
    public int size() {
        return headers.size();
    }

    /**
     * Check if  headers is empty.
     * @return {@link Boolean}
     */
    public boolean isEmpty() {
        return headers.isEmpty();
    }
}
