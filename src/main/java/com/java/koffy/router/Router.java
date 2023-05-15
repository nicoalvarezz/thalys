package com.java.koffy.router;

import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
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

    private Map<HttpMethod, Map<String, Supplier<Object>>> rotues = new HashMap<>();
    private Object action;

    public void get(String uri, Supplier<Object> action) {
        this.rotues.put(HttpMethod.GET, new HashMap<>(){{ put(uri, action); }});
    }

    public void post(String uri, Supplier<Object> action) {
        this.rotues.put(HttpMethod.POST, new HashMap<>(){{ put(uri, action); }});
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (rotues.get(HttpMethod.valueOf(method)).get(uri) == null) {
            throw new HttpNotFoundException("Method not found");
        }

        this.action = rotues.get(HttpMethod.valueOf(method)).get(uri).get();

        httpServletResponse.setContentType("text/plain");
        httpServletResponse.getWriter().println(action);
        request.setHandled(true);
    }
}



