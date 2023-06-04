package com.java.koffy.http;

import com.java.koffy.App;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    private App app = App.bootstrap();

    @Test
    public void testRequestReturnsDataObtainedFromServer() {
        String uri = "/test/route";
        app.router().post(uri, (request -> ResponseEntity.textResponse("POST OK").status(200).build()));

        Map<String, String> queryParams = new HashMap<>() {{
            put("a", "1");
            put("b", "2");
            put("test", "foo");
        }};
        Map<String, String> postData = new HashMap<>() {{
            put("post", "test");
            put("foo", "bar");
        }};

        RequestEntity request = RequestEntity.builder()
                .uri(uri)
                .method(HttpMethod.POST)
                .queryData(queryParams)
                .postData(postData)
                .build();

        assertEquals(uri, request.getUri());
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(queryParams, request.getQueryData());
        assertEquals(postData, request.getPostData());
    }

    @Test
    public void testRequestPostDataReturnsValueIfKeyGiven() {
        String uri = "/test/route";
        app.router().post(uri, (request -> ResponseEntity.textResponse("POST OK").status(200).build()));
        Map<String, String> postData = new HashMap<>() {{
            put("post", "test");
            put("foo", "bar");
        }};

        RequestEntity request = RequestEntity.builder()
                .uri(uri)
                .method(HttpMethod.POST)
                .postData(postData)
                .build();

        ArrayList<String> expectedData = new ArrayList<>() {{
            add("test");
            add("bar");
        }};

        assertEquals(expectedData.get(0), request.getPostData("post").get());
        assertEquals(expectedData.get(1), request.getPostData("foo").get());
        assertFalse(request.getPostData("does not exist").isPresent());
    }

    @Test
    public void testRequestQueryDataReturnsValueIfKeyGiven() {
        String uri = "/test/route";
        app.router().post(uri, (request -> ResponseEntity.textResponse("POST OK").status(200).build()));
        Map<String, String> queryParams = new HashMap<>() {{
            put("a", "1");
            put("test", "foo");
        }};

        RequestEntity request = RequestEntity.builder()
                .uri(uri)
                .method(HttpMethod.POST)
                .queryData(queryParams)
                .build();

        ArrayList<String> expectedData = new ArrayList<>() {{
            add("1");
            add("foo");
        }};

        assertEquals(expectedData.get(0), request.getQueryData("a").get());
        assertEquals(expectedData.get(1), request.getQueryData("test").get());
        assertFalse(request.getQueryData("does not exit").isPresent());
    }

    @Test
    public void testRouteParams() {
        App app = App.bootstrap();
        app.router().get("/test/{test}/foo/{bar}", (request) -> ResponseEntity.textResponse("GET OK").status(200).build());
        String uri = "/test/1/foo/2";

        RequestEntity request = RequestEntity.builder()
                .uri(uri)
                .method(HttpMethod.GET)
                .build();

        ArrayList<String> expectedData = new ArrayList<>() {{
            add("1");
            add("2");
        }};

        assertEquals(expectedData.get(0), request.routeParams().get("test"));
        assertEquals(expectedData.get(1), request.routeParams().get("bar"));
        assertNull(request.routeParams().get("does not exit"));
    }
}
