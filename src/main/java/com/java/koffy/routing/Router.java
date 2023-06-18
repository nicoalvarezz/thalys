package com.java.koffy.routing;

import com.java.koffy.http.HttpMethod;
import com.java.koffy.exception.HttpNotFoundException;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.http.Middleware;
import com.java.koffy.utils.Validator;

import javax.validation.ConstraintViolationException;
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
     * @param action {@link Function<RequestEntity, ResponseEntity>} action assigned to the route
     * @return {@link Route}
     */
    private Route registerRoute(HttpMethod method, String uri, Function<RequestEntity, ResponseEntity> action) {
        Route route = new Route(uri, action);
        routes.get(method).add(route);
        return route;
    }

    private Route registerRoute(HttpMethod method, String uri,
                                Function<RequestEntity, ResponseEntity> action, Class<?> validatable) {
        Route route = new Route(uri, action);
        route.setValidatable(validatable);
        routes.get(method).add(route);
        return route;
    }

    /**
     * Register route for GET request.
     * @param uri request URI
     * @param action URI action
     */
    public Route get(String uri, Function<RequestEntity, ResponseEntity> action) {
        return registerRoute(HttpMethod.GET, uri, action);
    }

    /**
     * Register route for POST request.
     * @param uri request URI
     * @param action URI action
     */
    public Route post(String uri, Function<RequestEntity, ResponseEntity> action) {
        return registerRoute(HttpMethod.POST, uri, action);
    }

    /**
     * Register route for POST request with validatable.
     * @param uri request URI
     * @param action URI action
     * @param validatable Validatable class
     */
    public Route post(String uri, Function<RequestEntity, ResponseEntity> action, Class<?> validatable) {
        return registerRoute(HttpMethod.POST, uri, action, validatable);
    }

    /**
     * Register route for PUT request.
     * @param uri request URI
     * @param action URI action
     */
    public Route put(String uri, Function<RequestEntity, ResponseEntity> action) {
        return registerRoute(HttpMethod.PUT, uri, action);
    }

    /**
     * Register route for PATCH request.
     * @param uri request URI
     * @param action URI action
     */
    public Route patch(String uri, Function<RequestEntity, ResponseEntity> action) {
        return registerRoute(HttpMethod.PATCH, uri, action);
    }

    /**
     * Register route for DELETE request.
     * @param uri request URI
     * @param action URI action
     */
    public Route delete(String uri, Function<RequestEntity, ResponseEntity> action) {
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
     * It also validates the request body against the validatable class if one was given.
     * @param request {@link RequestEntity}
     * @return {@link ResponseEntity}
     * @throws HttpNotFoundException Route not found.
     */
    public ResponseEntity resolve(RequestEntity request) throws ConstraintViolationException, HttpNotFoundException {
        if (request.getRoute().isPresent()) {
            Route route = request.getRoute().get();
            if (route.getValidatable() != null) {
                request.setSerialized(Validator.validate(request.getPostData(), route.getValidatable()));
            }
            if (route.hasMiddlewares()) {
                return runMiddlewares(request, route.getMiddlewares(), route.getAction());
            }
            return route.getAction().apply(request);
        }
        throw new HttpNotFoundException("Route not found");
    }

    /**
     * Execute the middlewares assigned to the {@link Route}. Uses recursion to run the stack of middlewares.
     * @param request {@link RequestEntity}
     * @param middlewares {@link List<Middleware>}
     * @param target {@link Function< RequestEntity ,  ResponseEntity >} action of the middleware
     * @return {@link ResponseEntity}
     */
    public ResponseEntity runMiddlewares(RequestEntity request,
                                         List<Middleware> middlewares, Function<RequestEntity, ResponseEntity> target) {
        if (middlewares.isEmpty()) {
            return target.apply(request);
        }
        return middlewares.get(0).handle(request, (baseRequest)
                -> runMiddlewares(request, middlewares.subList(1, middlewares.size()), target));
    }
}
