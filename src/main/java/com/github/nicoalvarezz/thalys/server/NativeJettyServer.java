package com.github.nicoalvarezz.thalys.server;

import com.github.nicoalvarezz.thalys.exception.MissingApplicationProperty;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The `NativeJettyServer` class represents a native Jetty server instance for handling HTTP requests
 * within the Koffy framework. It extends `AbstractHandler` to provide request handling capabilities.
 */
public class NativeJettyServer extends AbstractHandler {

    /**
     * Jetty Sever.
     */
    private final Server jettyServer;

    private final RequestHandler requestHandler = new RequestHandler();

    private final ResponseHandler responseHandler = new ResponseHandler();

    private static Logger LOGGER  = LoggerFactory.getLogger(NativeJettyServer.class);

    public NativeJettyServer() {
        jettyServer = new Server();
        jettyServer.setHandler(this);
    }

    /**
     * Set the port the jetty server will be listening to.
     * @param serverPort port number
     */
    public void setPort(String serverPort) {
        ServerConnector connector = new ServerConnector(jettyServer);
        try {
            connector.setPort(Integer.parseInt(serverPort));
        } catch (Exception e) {
            throw new MissingApplicationProperty("server port");
        }
        jettyServer.addConnector(connector);
        LOGGER.info("Jetty running on port: {}", serverPort);
    }

    /**
     * Start server.
     * @throws Exception
     */
    public void startServer() throws Exception {
        jettyServer.start();
    }

    /**
     * Handles an incoming HTTP request by processing the target, performing request handling,
     * and generating an appropriate HTTP response.
     * This method is responsible for coordinating the handling of an incoming HTTP request within
     * the framework. It processes the target, delegates request handling to the `requestHandler`,
     * and subsequently handles the generated `RequestEntity` by invoking the `responseHandler`
     * to generate an appropriate response. Once processing is complete, it marks the request as handled.
     *
     * @param target The target of the request, typically a URL path.
     * @param request The incoming Jetty Request object representing the HTTP request.
     * @param httpServletRequest The servlet request object for the HTTP request.
     * @param httpServletResponse The servlet response object for the HTTP response.
     * @throws IOException if an I/O error occurs while handling the request or response.
     */
    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException {
        long startRequestTime = System.currentTimeMillis();

        RequestEntity requestEntity = requestHandler.handleRequest(request);
        responseHandler.handleResponse(httpServletResponse, requestEntity);
        request.setHandled(true);

        long endRequestTime = System.currentTimeMillis();
        LOGGER.info("Request processing time: {} ms", endRequestTime - startRequestTime);
    }
}
