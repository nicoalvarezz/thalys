package io.thalys.http;

import io.thalys.http.Headers.ContentType;
import io.thalys.http.Headers.HttpHeader;
import io.thalys.http.Headers.HttpHeaders;
import org.junit.jupiter.api.Assertions;
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
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_JSON.get());

        assertTrue(headers.containsHeader(HttpHeader.CONTENT_TYPE.get()));
        assertEquals(ContentType.APPLICATION_JSON.get(), headers.get(HttpHeader.CONTENT_TYPE.get()));
        assertEquals(1, headers.size());
    }

    @Test
    public void testAddMultipleHeaders() {
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_JSON.get());
        headers.add(HttpHeader.LOCATION.get(), "location");

        assertTrue(headers.containsHeader(HttpHeader.CONTENT_TYPE.get()));
        assertTrue(headers.containsHeader(HttpHeader.LOCATION.get()));
        assertEquals(ContentType.APPLICATION_JSON.get(), headers.get(HttpHeader.CONTENT_TYPE.get()));
        assertEquals(HttpHeader.LOCATION.get(), headers.get("location"));
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
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.IMAGE_JPEG.get());

        assertEquals(ContentType.IMAGE_JPEG.get(), headers.get(HttpHeader.CONTENT_TYPE.get()));
        assertNull(headers.get(HttpHeader.LOCATION.get()));
    }

    @Test
    public void testContainsHeader() {
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_XML.get());

        assertTrue(headers.containsHeader(HttpHeader.CONTENT_TYPE.get()));
        assertFalse(headers.containsHeader(HttpHeader.SERVER.get()));
    }

    @Test
    public void testRemoveHeader() {
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_XML.get());

        assertEquals(1, headers.size());

        headers.removeHeader(HttpHeader.CONTENT_TYPE.get());

        assertFalse(headers.containsHeader(HttpHeader.CONTENT_TYPE.get()));
        assertEquals(0, headers.size());
    }

    @Test
    public void testGetAllHeaderNames() {
        assertTrue(headers.getAllHeaderNames().isEmpty());

        String customHeader = "x-custom-header";
        String headerValue = "x-custom-header-value";

        headers.add(customHeader, headerValue);
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.TEXT_PLAIN.get());
        headers.add(HttpHeader.SERVER.get(), "Server");

        Set<String> headerNames = headers.getAllHeaderNames();

        assertEquals(customHeader, headerNames.stream().toList().get(2));
        assertEquals(HttpHeader.CONTENT_TYPE.get(), headerNames.stream().toList().get(1));
        assertEquals(HttpHeader.SERVER.get(), headerNames.stream().toList().get(0));
    }

    @Test
    public void testHeadersSize() {
        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.TEXT_PLAIN.get());
        assertEquals(1, headers.size());

        headers.add(HttpHeader.SERVER.get(), "Server");
        assertEquals(2, headers.size());

        headers.removeHeader(HttpHeader.CONTENT_TYPE.get());
        headers.removeHeader(HttpHeader.SERVER.get());
        assertEquals(0, headers.size());
    }

    @Test
    public void testHeadersIsEmpty() {
        assertTrue(headers.isEmpty());

        headers.add(HttpHeader.CONTENT_TYPE.get(), ContentType.TEXT_PLAIN.get());
        assertFalse(headers.isEmpty());
    }

    @Test
    public void testHeaderOf() {
        assertEquals(HttpHeader.LOCATION, HttpHeader.headerOf("location"));
    }

    @Test
    public void testInvalidHeaderOf() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> HttpHeader.headerOf("something_something"));
    }
}
