package io.thalys.routing;

import io.thalys.http.Headers.HttpHeader;
import io.thalys.http.HttpStatus;
import io.thalys.routing.helpers.MockMiddleware;
import io.thalys.http.HttpMethod;
import io.thalys.http.RequestEntity;
import io.thalys.http.ResponseEntity;
import io.thalys.routing.helpers.MockMiddleware2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouterTest {

    private Router router;
    private final RequestEntity mockRequest = mock(RequestEntity.class);

    private ResponseEntity testsJsonResponse(String key, String value) {
        return ResponseEntity.jsonResponse(new HashMap<>() {{ put(key, value); }}, HttpStatus.OK);
    }

    @BeforeEach
    public void setUp() {
        router = new Router();
    }

    @Test
    public void testResolveBasicRoute() {
        String uri = "/test";
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", "test");

        router.get(uri, action);
        Route  route = router.resolveRoute(uri, HttpMethod.GET);

        assertEquals(uri, route.getUri());
        assertEquals(action, route.getAction());
        assertFalse(route.hasParameters());
    }

    @Test
    public void tesResolveMultipleBasicRoutes() {
        HashMap<String, Function<RequestEntity, ResponseEntity>> routes = new HashMap<>() {{
            put("/test", (request) -> testsJsonResponse("message", "test"));
            put("/foo", (request) -> testsJsonResponse("message", "test"));
            put("/bar", (request) -> testsJsonResponse("message", "test"));
            put("/long/nested/route", (request) -> testsJsonResponse("message", "test"));
        }};

        for (String uri : routes.keySet()) {
            router.get(uri, routes.get(uri));
            Route route = router.resolveRoute(uri, HttpMethod.GET);

            assertEquals(uri, route.getUri());
            assertEquals(routes.get(uri), route.getAction());
            assertFalse(route.hasParameters());

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
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", actionReturn);
        router.post(uri, action);

        Route  route = router.resolveRoute(uri, HttpMethod.POST);

        assertEquals(uri, route.getUri());
        assertEquals(action, route.getAction());
        assertFalse(route.hasParameters());

    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPutMethod(String uri, String actionReturn) {
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", actionReturn);
        router.put(uri, action);

        Route  route = router.resolveRoute(uri, HttpMethod.PUT);

        assertEquals(uri, route.getUri());
        assertEquals(action, route.getAction());
        assertFalse(route.hasParameters());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithPatchMethod(String uri, String actionReturn) {
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", actionReturn);
        router.patch(uri, action);

        Route  route = router.resolveRoute(uri, HttpMethod.PATCH);

        assertEquals(uri, route.getUri());
        assertEquals(action, route.getAction());
        assertFalse(route.hasParameters());
    }

    @ParameterizedTest
    @MethodSource("uriWithActionReturns")
    public void testResolveMultipleBasicRoutesWithDeleteMethod(String uri, String actionReturn) {
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", actionReturn);
        router.delete(uri, action);

        Route route = router.resolveRoute(uri, HttpMethod.DELETE);

        assertEquals(uri, route.getUri());
        assertEquals(action, route.getAction());
        assertFalse(route.hasParameters());
    }

    @Test
    public void testResolveEmptyRoute() {
        String uri = "empty-route";
        Route route = router.resolveRoute(uri, HttpMethod.DELETE);

        assertTrue(route.isEmpty());
    }

    @Test
    public void testRunMiddlewares() {
        String uri = "/test-middlewares";
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", "response");

        router.get(uri, action).setMiddlewares(new ArrayList(){{
            add(MockMiddleware.class);
        }});

        Route route = router.resolveRoute(uri, HttpMethod.GET);

        when(mockRequest.getRoute()).thenReturn(route);
        ResponseEntity expectedResponse = router.resolve(mockRequest);

        assertEquals(action, route.getAction());
        assertEquals(expectedResponse.getHeader(HttpHeader.SERVER).get(), "fake-test-server");
    }

    @Test
    public void testMiddlewareStackCanBeStopped() {
        String uri = "/test-middlewares";
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", "unreachable");

        Route actulaRoute = router.get(uri, action);
        actulaRoute.setMiddlewares(new ArrayList(){{
            add(MockMiddleware2.class);
            add(MockMiddleware.class);
        }});

        Route route = router.resolveRoute(uri, HttpMethod.GET);

        when(mockRequest.getRoute()).thenReturn(actulaRoute);
        ResponseEntity expectedResponse = router.resolve(mockRequest);

        assertEquals("Stopped", expectedResponse.getContent());
        assertFalse(expectedResponse.getHeader(HttpHeader.SERVER).isPresent());
    }

    @Test
    public void testResolveWithNoMiddlewares() {
        String uri = "/test-middlewares";
        Function<RequestEntity, ResponseEntity> action = (request) -> testsJsonResponse("message", "unreachable");

        router.get(uri, action);

        Route route = router.resolveRoute(uri, HttpMethod.GET);

        when(mockRequest.getRoute()).thenReturn(route);
        ResponseEntity expectedResponse = router.resolve(mockRequest);

        assertEquals(action, route.getAction());
        assertEquals(expectedResponse.getStatus(), HttpStatus.OK);
    }

    @Test
    public void testResolveMiddlewaresWithEmptyRoute() {
        when(mockRequest.getRoute()).thenReturn(new Route());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            router.resolve(mockRequest);
        });

        assertEquals("Route not found", exception.getMessage());
    }
}
