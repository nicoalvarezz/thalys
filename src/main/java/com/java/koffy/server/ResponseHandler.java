package com.java.koffy.server;

import com.java.koffy.container.Container;
import com.java.koffy.exception.RouteNotFoundException;
import com.java.koffy.http.HttpStatus;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.routing.Router;
import jakarta.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * The `ResponseHandler` class is responsible for handling server responses within the Koffy framework.
 * It constructs and manages the HTTP response to be sent back to clients after processing incoming requests.
 */
public class ResponseHandler {

    /**
     * The router used to resolve routes and handle requests.
     */
    private Router router = Container.resolve(Router.class);

    /**
     * Handles the server's response by setting the appropriate status, returning content,
     * and setting response headers based on the provided request.
     * This method constructs the response using the provided `RequestEntity` and writes the response
     * content, status, and headers to the `HttpServletResponse` object.
     *
     * @param servletResponse The HttpServletResponse object for the HTTP response.
     * @param request The RequestEntity representing the incoming request.
     * @throws IOException if an I/O error occurs while handling the response.
     */
    public void handleResponse(HttpServletResponse servletResponse, RequestEntity request) throws IOException {
        ResponseEntity response = constructResponse(request);

        servletResponse.setStatus(response.getStatus().statusCode());
        servletResponse.getWriter().println(response.getContent());
        for (String header : response.getHttpHeaders().getAllHeaderNames()) {
            servletResponse.addHeader(header, response.getHttpHeaders().get(header));
        }
    }

    /**
     * Constructs the appropriate ResponseEntity based on the provided request.
     * This method attempts to resolve the request using the router. If a matching route is found,
     * it returns the corresponding ResponseEntity. If no matching route is found, a NOT_FOUND
     * response is generated. In case of a constraint violation, a BAD_REQUEST response is generated.
     *
     * @param request The RequestEntity representing the incoming request.
     * @return A ResponseEntity representing the constructed response.
     */
    private ResponseEntity constructResponse(RequestEntity request) {
        try {
            return router.resolve(request);
        } catch (RouteNotFoundException e) {
            return ResponseEntity.textResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.textResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
