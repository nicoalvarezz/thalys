package com.java.koffy.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.routing.Router;
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

public class NativeJettyServer extends AbstractHandler implements Server {

    private String uri;
    private HttpMethod method;
    private Map<String, String> data;
    private Map<String, String> query;
    private KoffyRequest koffyRequest;
    private KoffyResponse koffyResponse;
    private final org.eclipse.jetty.server.Server jettyServer;
    private static ObjectMapper MAPPER = new ObjectMapper();
    private Router router;

    public NativeJettyServer(int serverPort) {
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
    public String getUri() {
        return uri;
    }

    @Override
    public HttpMethod getRequestMethod() {
        return method;
    }

    @Override
    public Map<String, String> getPostData() {
        return data;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return query;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        createRequest(request, httpServletRequest);
        createResponse();

        httpServletResponse.setStatus(koffyResponse.getStatus());

        // I need to rethink logic here
        if (koffyResponse.getContent() != null) {
            httpServletResponse.getWriter().println(koffyResponse.getContent());
        }

        for (String header : koffyResponse.getHeaders().keySet()) {
            httpServletResponse.addHeader(header, koffyResponse.getHeaders().get(header));
        }
        request.setHandled(true);
    }

    private void createRequest(Request request, HttpServletRequest httpServletRequest) throws IOException {
        koffyRequest = KoffyRequest.builder()
                .uri(request.getRequestURI())
                .method(HttpMethod.valueOf(request.getMethod()))
                .postData(parsePostData(httpServletRequest))
                .queryData(parseQueryData(httpServletRequest))
                .build();
    }

    private void createResponse() {
        try {
            koffyResponse = router.resolve(koffyRequest).getAction().get();
        } catch (HttpNotFoundException e) {
            koffyResponse = KoffyResponse.textResponse(404, e.getMessage());
        }
    }

    private Map<String, String> parsePostData(HttpServletRequest request) throws IOException {
        String data = new BufferedReader(new InputStreamReader(request.getInputStream())).readLine();

        if (data == null) {
            return new HashMap<>();
        }

        if (data.contains("&")) {
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
        // Convert url-encoded formatted string to Map<String, String> object
        return Arrays.stream(data.split("&"))
                .map(param -> param.split("="))
                .collect(HashMap::new, (m, arr) -> m.put(arr[0], arr[1]), HashMap::putAll);
    }
}
