package com.java.koffy.router;

import com.java.koffy.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouterTest {

    private Router router;
    private Request mockRequest;
    private HttpServletRequest mockHttpServletRequest;
    private HttpServletResponse mockHttpServletResponse;
    private PrintWriter mockPrintWriter;

    @BeforeEach
    void setUp() {
        router = new Router();
        mockRequest = mock(Request.class);
        mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletResponse = mock(HttpServletResponse.class);
        mockPrintWriter = mock(PrintWriter.class);
    }

    private void mockingRouterHandling(String uri, HttpMethod method) throws IOException, ServletException {
        when(mockRequest.getMethod()).thenReturn(method.toString());
        when(mockRequest.getRequestURI()).thenReturn(uri);
        when(mockHttpServletResponse.getWriter()).thenReturn(mockPrintWriter);
        router.handle(null, mockRequest, mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    public void testResolveBasicRoute() throws ServletException, IOException {
        String uri = "/test";
        Supplier<Object> action = () -> "test";

        router.get(uri, action);
        mockingRouterHandling(uri, HttpMethod.GET);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(action.get(), router.getCurrentRoute().getAction().get());
        
    }

    @Test
    public void tesResolveMultipleBasicRoutes() throws IOException, ServletException {
        HashMap<String, Supplier<Object>> routes = new HashMap<>() {{
            put("/test", () -> "test");
            put("/foo", () -> "foo");
            put("/bar", () -> "bar");
            put("/long/nested/route", () -> "long nested route");
        }};

        for (String uri : routes.keySet()) {
            router.get(uri, routes.get(uri));
            mockingRouterHandling(uri, HttpMethod.GET);

            assertEquals(uri, router.getCurrentRoute().getUri());
            assertEquals(routes.get(uri).get(), router.getCurrentRoute().getAction().get());

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
    public void testResolveMultipleBasicRoutesWithPostMethod(String uri, String actionReturn) throws IOException, ServletException {
        router.post(uri, () -> actionReturn);
        mockingRouterHandling(uri, HttpMethod.POST);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(actionReturn, router.getCurrentRoute().getAction().get());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPutMethod(String uri, String actionReturn) throws IOException, ServletException {
        router.put(uri, () -> actionReturn);
        mockingRouterHandling(uri, HttpMethod.PUT);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(actionReturn, router.getCurrentRoute().getAction().get());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPatchMethod(String uri, String actionReturn) throws IOException, ServletException {
        router.patch(uri, () -> actionReturn);
        mockingRouterHandling(uri, HttpMethod.PATCH);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(actionReturn, router.getCurrentRoute().getAction().get());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithDeleteMethod(String uri, String actionReturn) throws IOException, ServletException {
        router.delete(uri, () -> actionReturn);
        mockingRouterHandling(uri, HttpMethod.DELETE);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(actionReturn, router.getCurrentRoute().getAction().get());
    }
}
