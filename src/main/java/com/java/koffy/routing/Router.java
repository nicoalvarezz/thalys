package com.java.koffy.routing;

import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Router {

    private Map<HttpMethod, ArrayList<Route>> routes = new HashMap<>();

    public Router() {
        for (HttpMethod method : HttpMethod.values()) {
            routes.put(method, new ArrayList<>());
        }
    }

    private void registerRoute(HttpMethod method, String uri, Supplier<KoffyResponse> action) {
        routes.get(method).add(new Route(uri, action));
    }

    public void get(String uri, Supplier<KoffyResponse> action) {
        registerRoute(HttpMethod.GET, uri, action);
    }

    public void post(String uri, Supplier<KoffyResponse> action) {
        registerRoute(HttpMethod.POST, uri, action);
    }

    public void put(String uri, Supplier<KoffyResponse> action) {
        registerRoute(HttpMethod.PUT, uri, action);
    }

    public void patch(String uri, Supplier<KoffyResponse> action) {
        registerRoute(HttpMethod.PATCH, uri, action);
    }

    public void delete(String uri, Supplier<KoffyResponse> action) {
        registerRoute(HttpMethod.DELETE, uri, action);
    }

    public Route resolve(KoffyRequest request) {
        for (Route route : routes.get(request.getMethod())) {
            if (route.matches(request.getUri())) {
                return route;
            }
        }
        throw new HttpNotFoundException("Route not found");
    }
}
