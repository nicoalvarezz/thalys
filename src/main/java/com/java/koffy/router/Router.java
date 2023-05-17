package com.java.koffy.router;

import com.java.koffy.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Router extends AbstractHandler {

    private Map<HttpMethod, Map<String, Supplier<Object>>> routes = new HashMap<>();
    private Object action;

    public Router() {
        for (HttpMethod method : HttpMethod.values()) {
            routes.put(method, new HashMap<>());
        }
    }

    public void get(String uri, Supplier<Object> action) {
        routes.get(HttpMethod.GET).put(uri, action);
    }


    public void post(String uri, Supplier<Object> action) {
        routes.get(HttpMethod.POST).put(uri, action);
    }

    public void put(String uri, Supplier<Object> action) {
        routes.get(HttpMethod.PUT).put(uri, action);
    }

    public void patch(String uri, Supplier<Object> action) {
        routes.get(HttpMethod.PATCH).put(uri, action);
    }

    public void delete(String uri, Supplier<Object> action) {
        routes.get(HttpMethod.DELETE).put(uri, action);
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (routes.get(HttpMethod.valueOf(method)).get(uri) == null) {
            httpServletResponse.setStatus(404);
            httpServletResponse.setContentType("text/plain");
            return;
        }

        action = routes.get(HttpMethod.valueOf(method)).get(uri).get();

        httpServletResponse.setContentType("text/plain");
        httpServletResponse.getWriter().println(action);
        request.setHandled(true);
    }
}



