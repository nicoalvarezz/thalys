package com.java.koffy.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.router.Router;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server extends AbstractHandler {

    private String uri;

    private HttpMethod method;

    private Map<String, String> data;

    private Map<String, String> query;

    private static ObjectMapper MAPPER = new ObjectMapper();

    private com.java.koffy.http.Request koffyRequest = new com.java.koffy.http.Request();

    private final org.eclipse.jetty.server.Server jettyServer;

    private Router router;

    public Server(int serverPort) {
        this.jettyServer = new org.eclipse.jetty.server.Server(serverPort);
        this.jettyServer.setHandler(this);
        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(this);
    }

    public void startServer() throws Exception {
        jettyServer.start();
        jettyServer.join();
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        koffyRequest.setUri(request.getRequestURI());
        koffyRequest.setMethod(HttpMethod.valueOf(request.getMethod()));

        try {
            koffyRequest.setPostData(parsePostData(httpServletRequest));
        } catch (IOException e) {
            httpServletResponse.setStatus(404);
            httpServletResponse.setContentType("text/plain");
            request.setHandled(true);
        }
        koffyRequest.setQueryData(parseQueryData(httpServletRequest));

        httpServletResponse.getWriter().println(router.resolve(koffyRequest).getAction().get());
        httpServletResponse.setContentType("text/plain");
        request.setHandled(true);
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public com.java.koffy.http.Request getRequest() {
        return koffyRequest;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    private Map<String, String> parsePostData(HttpServletRequest request) throws IOException {

        String data = new BufferedReader(new InputStreamReader(request.getInputStream())).readLine();

        if (data == null) {
            return new HashMap<>();
        }

        if (data.contains("&")) {
            // Convert url-encoded formatted string to Map<String, String> object
            return parseUrlEncoded(data);
        }
        return MAPPER.readValue(data, Map.class);
    }

    private Map<String, String> parseQueryData(HttpServletRequest request) {
        if (request.getQueryString() != null) {
            return parseUrlEncoded(request.getQueryString());
        }
        return new HashMap<>();
    }

    private Map<String, String> parseUrlEncoded(String data) {
        return Arrays.stream(data.split("&"))
                .map(param -> param.split("="))
                .collect(HashMap::new, (m, arr) -> m.put(arr[0], arr[1]), HashMap::putAll);
    }
}
