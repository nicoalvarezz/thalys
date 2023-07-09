package com.java.koffy.server;

import com.java.koffy.container.Container;
import com.java.koffy.exception.RouteNotFound;
import com.java.koffy.http.HttpStatus;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.routing.Router;
import jakarta.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

public class ResponseHandler {

    private Router router = Container.resolve(Router.class);


    /**
     * Handle server response.
     * Set server status set in the action.
     * Return the contents.
     * Set the server response headers.
     * @param servletResponse
     * @param request
     * @throws IOException
     */
    public void handleResponse(HttpServletResponse servletResponse, RequestEntity request) throws IOException {
        ResponseEntity response = constructResponse(request);

        servletResponse.setStatus(response.getStatus().statusCode());
        servletResponse.getWriter().println(response.getContent());
        for (String header : response.getHttpHeaders().getAllHeaderNames()) {
            servletResponse.addHeader(header, response.getHttpHeaders().get(header));
        }
    }

    private ResponseEntity constructResponse(RequestEntity request) {
        try {
            return router.resolve(request);
        } catch (RouteNotFound e) {
            return ResponseEntity.textResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.textResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
