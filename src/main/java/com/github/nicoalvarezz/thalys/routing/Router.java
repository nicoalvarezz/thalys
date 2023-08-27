package com.github.nicoalvarezz.thalys.routing;

import com.github.nicoalvarezz.thalys.exception.RouteNotFoundException;
import com.github.nicoalvarezz.thalys.middlewares.Middleware;
import com.github.nicoalvarezz.thalys.http.HttpMethod;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static Logger LOGGER  = LoggerFactory.getLogger(Router.class);

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
     * If no route is found, an empty route is returned.
     * @param uri {@link String}
     * @param method {@link HttpMethod}
     * @return {@link Route}
     * @throws RouteNotFoundException http not found
     */
    public Route resolveRoute(String uri, HttpMethod method) {
        for (Route route : routes.get(method)) {
            if (route.matches(uri)) {
                return route;
            }
        }
        return new Route();
    }

    /**
     * Returns the response assigned to the action of the {@link Route}.
     * This method is also in charge of running the middlewares if this {@link Route} has any middleware assigned.
     * @param request {@link RequestEntity}
     * @return {@link ResponseEntity}
     * @throws RouteNotFoundException Route not found.
     */
    public ResponseEntity resolve(RequestEntity request) throws ConstraintViolationException, RouteNotFoundException {
        Route route = request.getRoute();
        if (!route.isEmpty()) {
            if (route.hasMiddlewares()) {
                return runMiddlewares(request, route.getMiddlewares(), route.getAction());
            }
            return route.getAction().apply(request);
        }
        throw new RouteNotFoundException("Route not found");
    }

    /**
     * Execute the middlewares assigned to the {@link Route}. Uses recursion to run the stack of middlewares.
     * @param request {@link RequestEntity}
     * @param middlewares {@link List<  Middleware  >}
     * @param target {@link Function< RequestEntity ,  ResponseEntity >} action of the middleware
     * @return {@link ResponseEntity}
     */
    public ResponseEntity runMiddlewares(RequestEntity request,
                                         List<Middleware> middlewares, Function<RequestEntity, ResponseEntity> target) {
        if (middlewares.isEmpty()) {
            return target.apply(request);
        }

        Middleware currentMiddleware = middlewares.get(0);
        LOGGER.info("Middleware - {} [Executing]", currentMiddleware.getClass().getSimpleName());

        return currentMiddleware.handle(request, (baseRequest) -> {
            ResponseEntity response = runMiddlewares(request, middlewares.subList(1, middlewares.size()), target);
            LOGGER.info("Middleware - {} [Completed]", currentMiddleware.getClass().getSimpleName());
            return response;
        });
    }
}
