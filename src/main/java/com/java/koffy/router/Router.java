package com.java.koffy.router;

import com.java.koffy.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Router extends AbstractHandler {

    private Map<HttpMethod, ArrayList<Route>> routes = new HashMap<>();
    private Object action;

    private Route currentRoute;

    public Router() {
        for (HttpMethod method : HttpMethod.values()) {
            routes.put(method, new ArrayList<>());
        }
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    private void registerRoute(HttpMethod method, String uri, Supplier<Object> action) {
        routes.get(method).add(new Route(uri, action));
    }

    public void get(String uri, Supplier<Object> action) {
        registerRoute(HttpMethod.GET, uri, action);
    }

    public void post(String uri, Supplier<Object> action) {
        registerRoute(HttpMethod.POST, uri, action);
    }

    public void put(String uri, Supplier<Object> action) {
        registerRoute(HttpMethod.PUT, uri, action);
    }

    public void patch(String uri, Supplier<Object> action) {
        registerRoute(HttpMethod.PATCH, uri, action);
    }

    public void delete(String uri, Supplier<Object> action) {
        registerRoute(HttpMethod.DELETE, uri, action);
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        for (Route route : routes.get(HttpMethod.valueOf(method))) {
            if (route.matches(uri)) {
                this.currentRoute = route;
                action = route.getAction().get();
                httpServletResponse.setContentType("text/plain");
                httpServletResponse.getWriter().println(action);
                request.setHandled(true);
            }
        }

        httpServletResponse.setStatus(404);
        httpServletResponse.setContentType("text/plain");
    }
}
