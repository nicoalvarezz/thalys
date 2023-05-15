package com.java.koffy;

import com.java.koffy.router.Router;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

public class App {

    private Server server;

    public App(int serverPort) {
        this.server = new Server(serverPort);
    }

    public Router router() {
        Router router = new Router();
        server.setHandler(router);

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(router);

        return router;
    }

    public void startServer() throws Exception {
        server.start();
        server.join();
    }

    public String getRequest() {
        return server.getRequestLog().toString();
    }
}
