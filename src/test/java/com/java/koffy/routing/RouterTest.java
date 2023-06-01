package com.java.koffy.routing;

import com.java.koffy.App;
import com.java.koffy.server.ServerImpl;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouterTest {

    private Router router = App.bootstrap().router();
    private final ServerImpl mockServer = mock(ServerImpl.class);
    private final KoffyRequest mockRequest = mock(KoffyRequest.class);

    private void mockingRouterHandling(String uri, HttpMethod method) {
        when(mockServer.getRequest()).thenReturn(
                KoffyRequest.builder()
                    .uri(uri)
                    .method(method)
                    .build());
        when(mockRequest.getMethod()).thenReturn(method);
        when(mockRequest.getUri()).thenReturn(uri);
    }

    private KoffyResponse testsJsonResponse(String key, String value) {
        return KoffyResponse.jsonResponse(200, new HashMap<>() {{ put(key, value); }});
    }

    private String actualContent() {
        return router.resolveRoute(mockRequest.getUri(), mockRequest.getMethod()).getAction().apply(mockRequest).getContent();
    }

    @Test
    public void testResolveBasicRoute() {
        String uri = "/test";
        Function<KoffyRequest, KoffyResponse> action = (request) -> testsJsonResponse("message", "test");

        router.get(uri, action);
        mockingRouterHandling(uri, HttpMethod.GET);

        assertEquals(uri, mockServer.getRequest().getUri());
        assertEquals(HttpMethod.GET, mockServer.getRequest().getMethod());
        assertEquals(action.apply(mockRequest).getContent(), actualContent());
    }

    @Test
    public void tesResolveMultipleBasicRoutes() {
        HashMap<String, Function<KoffyRequest, KoffyResponse>> routes = new HashMap<>() {{
            put("/test", (request) -> testsJsonResponse("message", "test"));
            put("/foo", (request) -> testsJsonResponse("message", "test"));
            put("/bar", (request) -> testsJsonResponse("message", "test"));
            put("/long/nested/route", (request) -> testsJsonResponse("message", "test"));
        }};

        for (String uri : routes.keySet()) {
            router.get(uri, routes.get(uri));
            mockingRouterHandling(uri, HttpMethod.GET);

            assertEquals(uri, mockServer.getRequest().getUri());
            assertEquals(HttpMethod.GET, mockServer.getRequest().getMethod());
            assertEquals(routes.get(uri).apply(mockRequest).getContent(), actualContent());

        }
    }

    private static Stream<Arguments> uriWithActionReturns() {
        return Stream.of(
            Arguments.of("/", "koffy"),
            Arguments.of("/test", "something"),
            Arguments.of("/test/nested", "nested"),
            Arguments.of("/test/another/nested", "nested nested"),
            Arguments.of("/test/another/nested/route", "another nested route"),
            Arguments.of("/test/another/nested/very/nested/route", "another very nested route"),
            Arguments.of("/test", "get"),
            Arguments.of("/random/get", "get"),
            Arguments.of("/test", "post"),
            Arguments.of("/random/nested/post", "post"),
            Arguments.of("/test", "patch"),
            Arguments.of("/some/patch/route", "patch"),
            Arguments.of("/test", "put"),
            Arguments.of("/put/random/route", "put"),
            Arguments.of("/test", "delete"),
            Arguments.of("/d", "delete")
        );
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPostMethod(String uri, String actionReturn) {
        router.post(uri, (request) -> testsJsonResponse("message", actionReturn));
        mockingRouterHandling(uri, HttpMethod.POST);

        assertEquals(uri, mockServer.getRequest().getUri());
        assertEquals(HttpMethod.POST, mockServer.getRequest().getMethod());
        assertEquals(testsJsonResponse("message", actionReturn).getContent(), actualContent());


    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPutMethod(String uri, String actionReturn) {
        router.put(uri, (request) -> testsJsonResponse("message", actionReturn));
        mockingRouterHandling(uri, HttpMethod.PUT);

        assertEquals(uri, mockServer.getRequest().getUri());
        assertEquals(HttpMethod.PUT, mockServer.getRequest().getMethod());
        assertEquals(testsJsonResponse("message", actionReturn).getContent(), actualContent());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPatchMethod(String uri, String actionReturn) {
        router.patch(uri, (request) -> testsJsonResponse("message", actionReturn));
        mockingRouterHandling(uri, HttpMethod.PATCH);

        assertEquals(uri, mockServer.getRequest().getUri());
        assertEquals(HttpMethod.PATCH, mockServer.getRequest().getMethod());
        assertEquals(testsJsonResponse("message", actionReturn).getContent(), actualContent());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithDeleteMethod(String uri, String actionReturn) {
        router.delete(uri, (request) -> testsJsonResponse("message", actionReturn));
        mockingRouterHandling(uri, HttpMethod.DELETE);

        assertEquals(uri, mockServer.getRequest().getUri());
        assertEquals(HttpMethod.DELETE, mockServer.getRequest().getMethod());
        assertEquals(testsJsonResponse("message", actionReturn).getContent(), actualContent());
    }
}
