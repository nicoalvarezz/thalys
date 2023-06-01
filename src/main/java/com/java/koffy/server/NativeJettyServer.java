package com.java.koffy.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.http.Header;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.HttpNotFoundException;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.routing.Router;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class NativeJettyServer extends AbstractHandler implements ServerImpl {

    /**
     * HTTP request.
     */
    private KoffyRequest koffyRequest;

    /**
     * Server response.
     */
    private KoffyResponse koffyResponse;

    /**
     * Jetty Sever.
     */
    private final Server jettyServer;

    /**
     * Jackson mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * HTTP Router.
     */
    private Router router;

    public NativeJettyServer() {
        this.jettyServer = new Server();
        this.jettyServer.setHandler(this);
        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(this);
    }

    /**
     * Set the port the jetty server will be listening to.
     * @param serverPort port number
     */
    public void setPort(int serverPort) {
        ServerConnector connector = new ServerConnector(jettyServer);
        connector.setPort(serverPort);
        jettyServer.addConnector(connector);
    }

    /**
     * Start server.
     * @throws Exception
     */
    public void startServer() throws Exception {
        jettyServer.start();
        jettyServer.join();
    }

    /**
     * Add routes to the server.
     * @param router HTTP router that contains all the set routes
     */
    public void setRouter(final Router router) {
        this.router = router;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KoffyRequest getRequest() {
        return koffyRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KoffyResponse getResponse() {
        return koffyResponse;
    }

    /**
     * Handles all the requests to the server, and its responses.
     * {@inheritDoc}
     */
    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException {
        koffyRequest = createRequest(request, httpServletRequest);
        koffyResponse = createResponse();

        httpServletResponse.setStatus(koffyResponse.getStatus());

        if (koffyResponse.getContent() != null) {
            httpServletResponse.getWriter().println(koffyResponse.getContent());
        }

        for (Header header : koffyResponse.getHeaders().keySet()) {
            httpServletResponse.addHeader(header.get(), koffyResponse.getHeaders().get(header));
        }
        request.setHandled(true);
    }

    private KoffyRequest createRequest(Request request,
                                       HttpServletRequest httpServletRequest) throws IOException {
        return KoffyRequest.builder()
                .uri(request.getRequestURI())
                .method(HttpMethod.valueOf(request.getMethod()))
                .headers(resolveHeaders(httpServletRequest))
                .postData(parsePostData(httpServletRequest))
                .queryData(parseQueryData(httpServletRequest))
                .build();
    }

    private Map<Header, String> resolveHeaders(HttpServletRequest httpServletRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<Header, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement().replace("-", "_");
            headers.put(Header.valueOf(headerName.toUpperCase()), httpServletRequest.getHeader(headerName));
        }
        return headers;
    }

    private KoffyResponse createResponse() {
        try {
            return router.resolve(koffyRequest).apply(koffyRequest);
        } catch (HttpNotFoundException e) {
            return KoffyResponse.textResponse(e.getMessage()).status(404).build();
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
