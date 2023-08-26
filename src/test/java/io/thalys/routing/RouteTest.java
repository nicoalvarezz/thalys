package io.thalys.routing;

import io.thalys.http.HttpStatus;
import io.thalys.http.ResponseEntity;
import io.thalys.middlewares.Middleware;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class RouteTest {

    private static Stream<Arguments> routeWithNoParameters() {
        return Stream.of(
                Arguments.of("/"),
                Arguments.of("/test"),
                Arguments.of("/test/nested"),
                Arguments.of("/test/another/nested"),
                Arguments.of("/test/another/nested/route"),
                Arguments.of("/test/another/nested/very/nested/route")
        );
    }

    private static Stream<Arguments> routeWithParameters() {
        return Stream.of(
                Arguments.of("/test/{test}", "/test/1", new HashMap<>(){{ put("test", "1"); }}),
                Arguments.of("/users/{user}", "/users/2", new HashMap<>(){{ put("user", "2"); }}),
                Arguments.of("/test/{test}", "/test/string",new HashMap<>(){{ put("test", "string"); }}),
                Arguments.of("/test/nested/{route}", "/test/nested/5", new HashMap<>(){{ put("route", "5"); }}),
                Arguments.of("/test/{param}/long/{test}/with/{multiple}/params", "/test/82382/long/5/with/yellow/params", new HashMap<>() {{
                    put("param", "82382");
                    put("test", "5");
                    put("multiple", "yellow");
                }})
        );
    }

    private ResponseEntity testsJsonResponse(String key, String value) {
        return ResponseEntity.jsonResponse(new HashMap<>() {{ put(key, value); }}, HttpStatus.OK);
    }

    @ParameterizedTest
    @MethodSource("routeWithNoParameters")
    public void testRegexWithNoParameters(String uri) {
        Route route = new Route(uri, (request) -> testsJsonResponse("message", "test"));
        assertTrue(route.matches(uri));
        assertFalse(route.matches(uri + "/extra/path"));
        assertFalse(route.matches("/extra/path" + uri));
        assertFalse(route.matches("randomRoute"));
    }

    @ParameterizedTest
    @MethodSource("routeWithNoParameters")
    public void testRegexOnUriThatEndsWithSlash(String uri) {
        Route route = new Route(uri, (request) -> testsJsonResponse("message", "test"));
        assertTrue(route.matches(uri + "/"));
    }

    @ParameterizedTest
    @MethodSource("routeWithParameters")
    public void testRegexWithParameters(String definition, String uri) {
        Route route = new Route(definition, (request) -> testsJsonResponse("message", "test"));
        assertTrue(route.matches(uri));
        assertFalse(route.matches(uri + "/extra/path"));
        assertFalse(route.matches("/extra/path" + uri));
        assertFalse(route.matches("randomRoute"));
    }

    @ParameterizedTest
    @MethodSource("routeWithParameters")
    public void testParseParameters(String definition, String uri, Map<String, String> expectedParameters) {
        Route route = new Route(definition, (request) -> testsJsonResponse("message", "test"));
        assertTrue(route.hasParameters());
        assertEquals(expectedParameters, route.parseParameter(uri));
    }

    @Test
    public void testEmptyRoute() {
        Route emptyRoute = new Route();
        Route route = new Route("/", (request) -> testsJsonResponse("message", "test"));

        assertTrue(emptyRoute.isEmpty());
        assertFalse(route.isEmpty());
    }

    @Test
    public void testSetMiddlewaresThrowsException() {
        Route route = new Route("/", (request) -> testsJsonResponse("message", "test"));

        List<Class<? extends Middleware>> middlewares = new ArrayList<>();
        middlewares.add(MiddlewareException.class); // Provide an abstract class or interface

        assertThrows(RuntimeException.class, () -> route.setMiddlewares(middlewares));
    }

    abstract class MiddlewareException implements Middleware {

    }
}
