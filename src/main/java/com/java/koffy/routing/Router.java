package com.java.koffy.routing;

import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * HTTP router.
 */
public class Router {

    /**
     * HTTP routes.
     */
    private Map<HttpMethod, ArrayList<Route>> routes = new HashMap<>();

    public Router() {
        for (HttpMethod method : HttpMethod.values()) {
            routes.put(method, new ArrayList<>());
        }
    }

    private void registerRoute(HttpMethod method, String uri, Function<KoffyRequest, KoffyResponse> action) {
        routes.get(method).add(new Route(uri, action));
    }

    /**
     * Register route for GET request.
     * @param uri request URI
     * @param action URI action
     */
    public void get(String uri, Function<KoffyRequest, KoffyResponse> action) {
        registerRoute(HttpMethod.GET, uri, action);
    }

    /**
     * Register route for POST request.
     * @param uri request URI
     * @param action URI action
     */
    public void post(String uri, Function<KoffyRequest, KoffyResponse> action) {
        registerRoute(HttpMethod.POST, uri, action);
    }

    /**
     * Register route for PUT request.
     * @param uri request URI
     * @param action URI action
     */
    public void put(String uri, Function<KoffyRequest, KoffyResponse> action) {
        registerRoute(HttpMethod.PUT, uri, action);
    }

    /**
     * Register route for PATCH request.
     * @param uri request URI
     * @param action URI action
     */
    public void patch(String uri, Function<KoffyRequest, KoffyResponse> action) {
        registerRoute(HttpMethod.PATCH, uri, action);
    }

    /**
     * Register route for DELETE request.
     * @param uri request URI
     * @param action URI action
     */
    public void delete(String uri, Function<KoffyRequest, KoffyResponse> action) {
        registerRoute(HttpMethod.DELETE, uri, action);
    }

    /**
     * Resolve the route of the request.
     * @param uri {@link String}
     * @param method {@link HttpMethod}
     * @return {@link Route}
     * @throws HttpNotFoundException http not found
     */
    public Route resolve(String uri, HttpMethod method) {
        for (Route route : routes.get(method)) {
            if (route.matches(uri)) {
                return route;
            }
        }
        throw new HttpNotFoundException("Route not found");
    }
}
