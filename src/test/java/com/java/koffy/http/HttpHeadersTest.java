package com.java.koffy.http;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpHeadersTest {

    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void testAddHeaders() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.JSON.get());

        assertTrue(headers.containsHeader(HttpHeaders.CONTENT_TYPE));
        assertEquals(ContentType.JSON.get(), headers.get(HttpHeaders.CONTENT_TYPE));
        assertEquals(1, headers.size());
    }

    @Test
    public void testAddMultipleHeaders() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.JSON.get());
        headers.add(HttpHeaders.LOCATION, "location");

        assertTrue(headers.containsHeader(HttpHeaders.CONTENT_TYPE));
        assertTrue(headers.containsHeader(HttpHeaders.LOCATION));
        assertEquals(ContentType.JSON.get(), headers.get(HttpHeaders.CONTENT_TYPE));
        assertEquals(HttpHeaders.LOCATION, headers.get("location"));
        assertEquals(2, headers.size());
    }

    @Test
    public void testAddCustomHeader() {
        String customHeader = "x-custom-header";
        String headerValue = "x-custom-header-value";
        headers.add(customHeader, headerValue);

        assertTrue(headers.containsHeader(customHeader));
        assertEquals(headerValue, headers.get(customHeader));
        assertEquals(1, headers.size());
    }

    @Test
    public void testAddMultipleCustomHeader() {
        String customHeader = "x-custom-header";
        String headerValue = "x-custom-header-value";
        String customHeader2 = "x-custom-header-2";
        String headerValue2 = "x-custom-header-value-2";

        headers.add(customHeader, headerValue);
        headers.add(customHeader2, headerValue2);

        assertTrue(headers.containsHeader(customHeader));
        assertEquals(headerValue, headers.get(customHeader));
        assertTrue(headers.containsHeader(customHeader2));
        assertEquals(headerValue2, headers.get(customHeader2));
        assertEquals(2, headers.size());
    }

    @Test
    public void testGetHeader() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.JPEG.get());

        assertEquals(ContentType.JPEG.get(), headers.get(HttpHeaders.CONTENT_TYPE));
        assertNull(headers.get(HttpHeaders.LOCATION));
    }

    @Test
    public void testContainsHeader() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.XML.get());

        assertTrue(headers.containsHeader(HttpHeaders.CONTENT_TYPE));
        assertFalse(headers.containsHeader(HttpHeaders.SERVER));
    }

    @Test
    public void testRemoveHeader() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.XML.get());

        assertEquals(1, headers.size());

        headers.removeHeader(HttpHeaders.CONTENT_TYPE);

        assertFalse(headers.containsHeader(HttpHeaders.CONTENT_TYPE));
        assertEquals(0, headers.size());
    }

    @Test
    public void testGetAllHeaderNames() {
        assertTrue(headers.getAllHeaderNames().isEmpty());

        String customHeader = "x-custom-header";
        String headerValue = "x-custom-header-value";

        headers.add(customHeader, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.TEXT.get());
        headers.add(HttpHeaders.SERVER, "Server");

        Set<String> headerNames = headers.getAllHeaderNames();

        assertEquals(customHeader, headerNames.stream().toList().get(2));
        assertEquals(HttpHeaders.CONTENT_TYPE, headerNames.stream().toList().get(1));
        assertEquals(HttpHeaders.SERVER, headerNames.stream().toList().get(0));
    }

    @Test
    public void testHeadersSize() {
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.TEXT.get());
        assertEquals(1, headers.size());

        headers.add(HttpHeaders.SERVER, "Server");
        assertEquals(2, headers.size());

        headers.removeHeader(HttpHeaders.CONTENT_TYPE);
        headers.removeHeader(HttpHeaders.SERVER);
        assertEquals(0, headers.size());
    }

    @Test
    public void testHeadersIsEmpty() {
        assertTrue(headers.isEmpty());

        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.TEXT.get());
        assertFalse(headers.isEmpty());
    }
}
