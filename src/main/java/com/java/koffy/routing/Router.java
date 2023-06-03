package com.java.koffy.routing;

import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.http.Middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    /**
     * Register {@link Route} to the router.
     * @param method {@link HttpMethod}
     * @param action {@link Function<KoffyRequest, KoffyResponse>} action assigned to the route
     * @return {@link Route}
     */
    private Route registerRoute(HttpMethod method, String uri, Function<KoffyRequest, KoffyResponse> action) {
        Route route = new Route(uri, action);
        routes.get(method).add(route);
        return route;
    }

    /**
     * Register route for GET request.
     * @param uri request URI
     * @param action URI action
     */
    public Route get(String uri, Function<KoffyRequest, KoffyResponse> action) {
        return registerRoute(HttpMethod.GET, uri, action);
    }

    /**
     * Register route for POST request.
     * @param uri request URI
     * @param action URI action
     */
    public Route post(String uri, Function<KoffyRequest, KoffyResponse> action) {
        return registerRoute(HttpMethod.POST, uri, action);
    }

    /**
     * Register route for PUT request.
     * @param uri request URI
     * @param action URI action
     */
    public Route put(String uri, Function<KoffyRequest, KoffyResponse> action) {
        return registerRoute(HttpMethod.PUT, uri, action);
    }

    /**
     * Register route for PATCH request.
     * @param uri request URI
     * @param action URI action
     */
    public Route patch(String uri, Function<KoffyRequest, KoffyResponse> action) {
        return registerRoute(HttpMethod.PATCH, uri, action);
    }

    /**
     * Register route for DELETE request.
     * @param uri request URI
     * @param action URI action
     */
    public Route delete(String uri, Function<KoffyRequest, KoffyResponse> action) {
        return registerRoute(HttpMethod.DELETE, uri, action);
    }

    /**
     * Resolve the route of the request. Checks if the route matches the URI,
     * and if so returns the {@link Route} assigned to a specific URI and Method.
     * If no route is found {@link Optional} is returned
     * @param uri {@link String}
     * @param method {@link HttpMethod}
     * @return {@link Route}
     * @throws HttpNotFoundException http not found
     */
    public Optional<Route> resolveRoute(String uri, HttpMethod method) {
        for (Route route : routes.get(method)) {
            if (route.matches(uri)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the response assigned to the action of the {@link Route}.
     * This method is also in charge of running the middlewares if this {@link Route} has any middleware assigned.
     * @param request {@link KoffyRequest}
     * @return {@link KoffyResponse}
     * @throws HttpNotFoundException Route not found.
     */
    public KoffyResponse resolve(KoffyRequest request) {
        if (request.getRoute().isPresent()) {
            Route route = request.getRoute().get();
            if (route.hasMiddlewares()) {
                return runMiddlewares(request, route.getMiddlewares(), route.getAction());
            }
            return route.getAction().apply(request);
        }
        throw new HttpNotFoundException("Route not found");
    }

    /**
     * Execute the middlewares assigned to the {@link Route}. Uses recursion to run the stack of middlewares.
     * @param request {@link KoffyRequest}
     * @param middlewares {@link List<Middleware>}
     * @param target {@link Function<KoffyRequest, KoffyResponse>} action of the middleware
     * @return {@link KoffyResponse}
     */
    public KoffyResponse runMiddlewares(KoffyRequest request,
                                        List<Middleware> middlewares, Function<KoffyRequest, KoffyResponse> target) {
        if (middlewares.isEmpty()) {
            return target.apply(request);
        }
        return middlewares.get(0).handle(request, (baseRequest)
                -> runMiddlewares(request, middlewares.subList(1, middlewares.size()), target));
    }
}
