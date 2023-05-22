package com.java.koffy.routing;

import com.java.koffy.Server.Server;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.Request;
import com.java.koffy.http.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouterTest {

    private Router router;
    private final Server mockServer = mock(Server.class);
    private final Request mockRequest = mock(Request.class);

    @BeforeEach
    void setUp() {
        router = new Router();
    }

    private void mockingRouterHandling(String uri, HttpMethod method) {
        when(mockServer.getRequestMethod()).thenReturn(method);
        when(mockServer.getUri()).thenReturn(uri);
        when(mockRequest.getMethod()).thenReturn(method);
        when(mockRequest.getUri()).thenReturn(uri);
    }

    private Response testsResponse(String content) {
        Response response = new Response();
        response.setContent(content);
        return response;
    }

    private String actualContent() {
        return router.resolve(mockRequest).getAction().get().getContent();
    }

    @Test
    public void testResolveBasicRoute() {
        String uri = "/test";
        Supplier<Response> action = () -> testsResponse("test");

        router.get(uri, action);
        mockingRouterHandling(uri, HttpMethod.GET);

        assertEquals(uri, mockServer.getUri());
        assertEquals(action.get().getContent(), actualContent());
    }

    @Test
    public void tesResolveMultipleBasicRoutes() {
        HashMap<String, Supplier<Response>> routes = new HashMap<>() {{
            put("/test", () -> testsResponse("test"));
            put("/foo", () -> testsResponse("foo"));
            put("/bar", () -> testsResponse("bar"));
            put("/long/nested/route", () -> testsResponse("long nested route"));
        }};

        for (String uri : routes.keySet()) {
            router.get(uri, routes.get(uri));
            mockingRouterHandling(uri, HttpMethod.GET);

            assertEquals(uri, mockServer.getUri());
            assertEquals(routes.get(uri).get().getContent(), actualContent());

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
        router.post(uri, () -> testsResponse(actionReturn));
        mockingRouterHandling(uri, HttpMethod.POST);

        assertEquals(uri, mockServer.getUri());
        assertEquals(testsResponse(actionReturn).getContent(), actualContent());


    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPutMethod(String uri, String actionReturn) {
        router.put(uri, () -> testsResponse(actionReturn));
        mockingRouterHandling(uri, HttpMethod.PUT);

        assertEquals(uri, mockServer.getUri());
        assertEquals(testsResponse(actionReturn).getContent(), actualContent());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPatchMethod(String uri, String actionReturn) {
        router.patch(uri, () -> testsResponse(actionReturn));
        mockingRouterHandling(uri, HttpMethod.PATCH);

        assertEquals(uri, mockServer.getUri());
        assertEquals(testsResponse(actionReturn).getContent(), actualContent());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithDeleteMethod(String uri, String actionReturn) {
        router.delete(uri, () -> testsResponse(actionReturn));
        mockingRouterHandling(uri, HttpMethod.DELETE);

        assertEquals(uri, mockServer.getUri());
        assertEquals(testsResponse(actionReturn).getContent(), actualContent());
    }
}
