package com.github.nicoalvarezz.thalys.middlewares;

import com.github.nicoalvarezz.thalys.autoconfigure.MiddlewareConfigs;
import com.github.nicoalvarezz.thalys.container.Container;
import com.github.nicoalvarezz.thalys.exception.MissingConfigException;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;

import java.util.function.Function;

/**
 * The `Authorization` class is a middleware component responsible for handling authorization logic
 * within the Koffy framework. It implements the `Middleware` interface and is designed to be inserted
 * into the middleware chain to enforce authorization checks before proceeding to the next middleware
 * or route handler.
 */
public class Authorization implements Middleware {

    /**
     * Handles the authorization process by checking if the incoming request is authorized. If authorized,
     * the middleware passes control to the next function in the chain. If not authorized, it returns an
     * unauthorized response.
     *
     * @param request The incoming {@link RequestEntity} to be authorized
     * @param next A functional interface representing the next middleware or routes handler in the chain
     * @return A {@link ResponseEntity} containing the result of the authorization process or an unauthorized response.
     */
    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        if (isAuthorised(request)) {
            return next.apply(request);
        }
        return ResponseEntity.unauthorized();
    }

    /**
     * Checks if the incoming request is authorized by invoking the configured authorization logic.
     * This logic is the configuration set up by the user of the framework to configure the middleware.
     *
     * @param request The incoming RequestEntity to be authorized.
     * @return `true` if the request is authorized, `false` otherwise.
     * @throws MissingConfigException if there is an issue resolving the AuthConfig from the container.
     */
    private boolean isAuthorised(RequestEntity request) {
        try {
            return Container.resolve(MiddlewareConfigs.class).getAuthConfig().isAuthorized(request);
        } catch (Exception e) {
            throw new MissingConfigException("AuthConfig");
        }
    }
}
