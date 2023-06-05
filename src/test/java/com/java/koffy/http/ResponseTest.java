package com.java.koffy.http;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    private String jsonEncode(Map<String, String> mapToEncode) {
        return new JSONObject(mapToEncode).toString();
    }

    @Test
    public void testJsonResponse() {
        Map<String, String> content = new HashMap<>() {{
            put("test", "foo");
            put("bar", "test");
        }};
        ResponseEntity response = ResponseEntity.jsonResponse(content).status(HttpStatus.OK).build();

        String expectedJson = jsonEncode(content);
        Map<Header, String> expectedHeaders = new HashMap<>() {{
            put(Header.CONTENT_TYPE, ContentType.JSON.get());
        }};

        assertEquals(HttpStatus.OK  , response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals(expectedJson, response.getContent());
    }

    @Test
    public void testTextResponse() {
        String content = "test message";
        ResponseEntity response = ResponseEntity.textResponse(content).status(HttpStatus.OK).build();

        Map<Header, String> expectedHeaders = new HashMap<>() {{
            put(Header.CONTENT_TYPE, ContentType.TEXT.get());
        }};

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testRedirectResponse() {
        String uri = "/redirect/test";
        ResponseEntity response = ResponseEntity.redirectResponse(uri);

        Map<Header, String> expectedHeaders = new HashMap<>() {{
            put(Header.LOCATION, uri);
        }};

        assertEquals(HttpStatus.FOUND, response.getStatus());
        assertEquals("", response.getContent());
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testDeleteResponseHeader() {
        String content = "test content";
        Map<Header, String> headers = new HashMap<>() {{
            put(Header.CONTENT_TYPE, ContentType.JSON.get());
            put(Header.SERVER, "Jetty");
        }};
        ResponseEntity response = ResponseEntity.textResponse(content).status(HttpStatus.OK).headers(headers).build();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(headers, response.getHeaders());
        assertEquals(2, response.getHeaders().size());

        response.removeHeader(Header.SERVER);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(headers, response.getHeaders());
        assertEquals(1, response.getHeaders().size());
        assertEquals(headers.get(Header.CONTENT_TYPE.get()), response.getHeaders().get(Header.CONTENT_TYPE.get()));
    }

    @Test
    public void testDeleteResponseHeaderFromJsonResponse() {
        Map<String, String> content = new HashMap<>() {{
            put("test", "foo");
            put("test2", "bar");
        }};
        Map<Header, String> headers = new HashMap<>() {{
            put(Header.CONTENT_TYPE, ContentType.JSON.get());
            put(Header.SERVER, "Jetty");
        }};
        ResponseEntity response = ResponseEntity.jsonResponse(content).status(HttpStatus.OK).headers(headers).build();

        String expectedJson = jsonEncode(content);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(headers, response.getHeaders());
        assertEquals(2, response.getHeaders().size());

        response.removeHeader(Header.SERVER);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(headers, response.getHeaders());
        assertEquals(1, response.getHeaders().size());
        assertEquals(headers.get(Header.CONTENT_TYPE.get()), response.getHeaders().get(Header.CONTENT_TYPE.get()));
    }
}
