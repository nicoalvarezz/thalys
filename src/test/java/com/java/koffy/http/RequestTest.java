package com.java.koffy.http;

import com.java.koffy.routing.Route;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestTest {

    @Test
    public void testRequestReturnsDataObtainedFromServer() {

        String uri = "/test/route";
        Map<String, String> queryParams = new HashMap<>() {{
            put("a", "1");
            put("b", "2");
            put("test", "foo");
        }};
        Map<String, String> postData = new HashMap<>() {{
            put("post", "test");
            put("foo", "bar");
        }};

        KoffyRequest request = KoffyRequest.builder()
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
        Map<String, String> postData = new HashMap<>() {{
            put("post", "test");
            put("foo", "bar");
        }};

        KoffyRequest request = KoffyRequest.builder()
                .uri(uri)
                .method(HttpMethod.POST)
                .postData(postData)
                .build();

        ArrayList<String> expectedData = new ArrayList<>() {{
            add("test");
            add("bar");
        }};

        assertEquals(expectedData.get(0), request.getPostData().get("post"));
        assertEquals(expectedData.get(1), request.getPostData().get("foo"));
        assertNull(request.getPostData().get("does not exit"));
    }

    @Test
    public void testRequestQueryDataReturnsValueIfKeyGiven() {
        String uri = "/test/route";
        Map<String, String> queryParams = new HashMap<>() {{
            put("a", "1");
            put("test", "foo");
        }};

        KoffyRequest request = KoffyRequest.builder()
                .uri(uri)
                .method(HttpMethod.POST)
                .queryData(queryParams)
                .build();

        ArrayList<String> expectedData = new ArrayList<>() {{
            add("1");
            add("foo");
        }};

        assertEquals(expectedData.get(0), request.getQueryData().get("a"));
        assertEquals(expectedData.get(1), request.getQueryData().get("test"));
        assertNull(request.getQueryData().get("does not exit"));
    }

    @Test
    public void testRouteParams() {
        Route route = new Route("/test/{test}/foo/{bar}", () -> KoffyResponse.textResponse(200, "GET OK"));
        String uri = "/test/1/foo/2";

        KoffyRequest request = KoffyRequest.builder()
                .route(route)
                .uri(uri)
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
