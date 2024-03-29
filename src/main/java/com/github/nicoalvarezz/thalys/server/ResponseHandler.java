package com.github.nicoalvarezz.thalys.server;

import com.github.nicoalvarezz.thalys.container.Container;
import com.github.nicoalvarezz.thalys.exception.RouteNotFoundException;
import com.github.nicoalvarezz.thalys.routing.Router;
import com.github.nicoalvarezz.thalys.http.HttpStatus;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger LOGGER  = LoggerFactory.getLogger(ResponseHandler.class);

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

        LOGGER.info("{} {} - Response sent (Status: {})",
                request.getMethod().get(), request.getUri(), response.getStatus().statusCode());
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
