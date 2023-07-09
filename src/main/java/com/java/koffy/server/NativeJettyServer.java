package com.java.koffy.server;

import com.java.koffy.http.RequestEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

public class NativeJettyServer extends AbstractHandler {

    /**
     * Jetty Sever.
     */
    private final Server jettyServer;

    private RequestHandler requestHandler = new RequestHandler();

    private ResponseHandler responseHandler = new ResponseHandler();

    public NativeJettyServer() {
        jettyServer = new Server();
        jettyServer.setHandler(this);
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
        RequestEntity requestEntity = requestHandler.handleRequest(request);
        responseHandler.handleResponse(httpServletResponse, requestEntity);
        request.setHandled(true);
    }
}
