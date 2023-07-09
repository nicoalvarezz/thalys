package com.java.koffy.http;

import com.java.koffy.http.Headers.ContentType;
import com.java.koffy.http.Headers.HttpHeader;
import com.java.koffy.http.Headers.HttpHeaders;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    private HttpHeaders expectedHeaders = new HttpHeaders();

    private String jsonEncode(Map<String, String> mapToEncode) {
        return new JSONObject(mapToEncode).toString();
    }

    @Test
    public void testJsonResponse() {
        Map<String, String> content = new HashMap<>() {{
            put("test", "foo");
            put("bar", "test");
        }};
        ResponseEntity response = ResponseEntity.jsonResponse(content, HttpStatus.OK);

        String expectedJson = jsonEncode(content);
        expectedHeaders.add(HttpHeader.CONTENT_TYPE, ContentType.APPLICATION_JSON.get());

        assertEquals(HttpStatus.OK  , response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
        assertEquals(expectedJson, response.getContent());
    }

    @Test
    public void testTextResponse() {
        String content = "test message";
        ResponseEntity response = ResponseEntity.textResponse(content, HttpStatus.OK);

        expectedHeaders.add(HttpHeader.CONTENT_TYPE, ContentType.TEXT_PLAIN.get());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
    }

    @Test
    public void testRedirectResponse() {
        String uri = "/redirect/test";
        ResponseEntity response = ResponseEntity.redirectResponse(uri);

        expectedHeaders.add(HttpHeader.LOCATION.get(), uri);

        assertEquals(HttpStatus.FOUND, response.getStatus());
        assertEquals("", response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
    }

    @Test
    public void testDeleteResponseHeader() {
        String content = "test content";
        expectedHeaders.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_JSON.get());
        expectedHeaders.add(HttpHeader.SERVER.get(), "Jetty");

        ResponseEntity response = ResponseEntity.textResponse(content, HttpStatus.OK, expectedHeaders);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
        assertEquals(2, response.getHttpHeaders().size());

        response.removeHeader(HttpHeader.SERVER);
        expectedHeaders.removeHeader(HttpHeader.SERVER.get());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
        assertEquals(1, response.getHttpHeaders().size());
        assertEquals(expectedHeaders.get(HttpHeader.CONTENT_TYPE.get()), response.getHttpHeaders().get(HttpHeader.CONTENT_TYPE.get()));
    }

    @Test
    public void testDeleteResponseHeaderFromJsonResponse() {
        Map<String, String> content = new HashMap<>() {{
            put("test", "foo");
            put("test2", "bar");
        }};
        expectedHeaders.add(HttpHeader.CONTENT_TYPE.get(), ContentType.APPLICATION_JSON.get());
        expectedHeaders.add(HttpHeader.SERVER, "Jetty");

        ResponseEntity response = ResponseEntity.jsonResponse(content, HttpStatus.OK, expectedHeaders);

        String expectedJson = jsonEncode(content);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
        assertEquals(2, response.getHttpHeaders().size());

        response.removeHeader(HttpHeader.SERVER);
        expectedHeaders.removeHeader(HttpHeader.SERVER.get());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedJson, response.getContent());
        assertEquals(expectedHeaders.headers(), response.getHttpHeaders().headers());
        assertEquals(1, response.getHttpHeaders().size());
        assertEquals(expectedHeaders.get(HttpHeader.CONTENT_TYPE.get()), response.getHttpHeaders().get(HttpHeader.CONTENT_TYPE.get()));
    }
}
