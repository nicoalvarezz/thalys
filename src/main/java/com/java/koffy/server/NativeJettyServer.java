package com.java.koffy.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.http.Headers.HttpHeaders;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.exception.HttpNotFoundException;
import com.java.koffy.http.HttpStatus;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.routing.Router;
import com.java.koffy.session.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;

import javax.validation.ConstraintViolationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class NativeJettyServer extends AbstractHandler implements HttpServer {

    /**
     * HTTP request.
     */
    private RequestEntity requestEntity;

    /**
     * Server response.
     */
    private ResponseEntity responseEntity;

    /**
     * Jetty Sever.
     */
    private final Server jettyServer;

    /**
     * Http Session.
     */
    private Session session;

    /**
     * Jackson mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * HTTP Router.
     */
    private Router router;

    public NativeJettyServer() {
        jettyServer = new Server();
        SessionHandler sessionHandler = sessionHandlerSetUp();
        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{sessionHandler, this});
        jettyServer.setHandler(handlerList);
    }

    /**
     * Returns the session handler.
     * This method is also in charge od setting up the sessionId manager, adn the max inactive interval.
     * This interval  is set to 30min by default.
     * @return {@link SessionHandler}
     */
    private SessionHandler sessionHandlerSetUp() {
        SessionIdManager sessionIdManager = new DefaultSessionIdManager(jettyServer);
        jettyServer.setSessionIdManager(sessionIdManager);
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setSessionIdManager(sessionIdManager);
        sessionHandler.setMaxInactiveInterval(30 * 60);
        return sessionHandler;
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
    public RequestEntity getRequest() {
        return requestEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity getResponse() {
        return responseEntity;
    }

    /**
     * Handles all the server requests and its responses.
     * It builds the request entity, so it can be used in the action definition.
     * It builds the response entity that is returned.
     * Sets the request as handled when the process is finished.
     * @param target Request URI
     * @param request Server Request
     * @param httpServletRequest Server httpServletRequest
     * @param httpServletResponse Server httpServletResponse
     * @throws IOException
     */
    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException {
        setSession(httpServletRequest.getSession());
        requestEntity = buildRequest(request);
        responseEntity = buildResponse();
        handleServerResponse(httpServletResponse);
        request.setHandled(true);
        session.handlerFlash();
    }

    /**
     * Set the session for the current request and client.
     * @param httpSession {@link HttpSession}
     */
    private void setSession(HttpSession httpSession) {
        this.session = new Session(httpSession);
    }

    /**
     * Return Http Session.
     * @return {@link Session}
     */
    public Session getSession() {
        return session;
    }

    /**
     * Handle server response.
     * Set server status set in the action.
     * Return the contents.
     * Set the server response headers.
     * @param response {@link HttpServletResponse}
     * @throws IOException
     */
    private void handleServerResponse(HttpServletResponse response) throws IOException {
        response.setStatus(responseEntity.getStatus().statusCode());
        response.getWriter().println(responseEntity.getContent());
        for (String header : responseEntity.getHeaders().keySet()) {
            response.addHeader(header, responseEntity.getHeaders().get(header));
        }
    }

    /**
     * Build {@link RequestEntity} from the server request.
     * @param request Server request
     * @return {@link RequestEntity}
     * @throws IOException
     */
    private RequestEntity buildRequest(Request request) throws IOException {
        return RequestEntity.builder()
                .uri(request.getRequestURI())
                .method(HttpMethod.valueOf(request.getMethod()))
                .headers(resolveHeaders(request))
                .postData(parsePostData(request))
                .queryData(parseQueryData(request))
                .build();
    }

    /**
     * Gather and save headers from the request.
     * @param request Server request
     * @return {@link Map} Headers
     */
    private HttpHeaders resolveHeaders(Request request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        HttpHeaders headers = new HttpHeaders();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName.toLowerCase(), request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * Build server {@link ResponseEntity}.
     * The response is built from the action of the URI called.
     * @return {@link ResponseEntity}
     */
    private ResponseEntity buildResponse() {
        try {
            return router.resolve(requestEntity);
        } catch (HttpNotFoundException e) {
            return ResponseEntity.textResponse(e.getMessage()).status(HttpStatus.NOT_FOUND).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.textResponse(e.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Parse post data received from the Server {@link Request}.
     * @param request Server request
     * @return {@link Map} post data
     * @throws IOException
     */
    private Map<String, String> parsePostData(Request request) throws IOException {
        String data = new BufferedReader(new InputStreamReader(request.getInputStream())).readLine();

        if (data == null) {
            return new HashMap<>();
        }

        // consider application/x-www-form-urlencoded header check. It is a more sophisticated approach
        // curl -X POST -d "key1=value1&key2=value2" -H "Content-Type:
        // application/x-www-form-urlencoded" http://example.com/api/endpoint
        if (data.contains("&")) {
            return parseUrlEncoded(data);
        }
        return MAPPER.readValue(data, Map.class);
    }

    /**
     * Parse query data received from the Server {@link Request}.
     * @param request Server request
     * @return {@link Map} Query data
     */
    private Map<String, String> parseQueryData(Request request) {
        if (request.getQueryString() != null) {
            return parseUrlEncoded(request.getQueryString());
        }
        return new HashMap<>();
    }

    /**
     * Return url-encoded formatted string to {@link Map}.
     * @param data url-encoded format
     * @return {@link Map} data
     */
    private Map<String, String> parseUrlEncoded(String data) {
        return Arrays.stream(data.split("&"))
                .map(param -> param.split("="))
                .collect(HashMap::new, (m, arr) -> m.put(arr[0], arr[1]), HashMap::putAll);
    }
}
