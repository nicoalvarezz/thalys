package com.java.koffy;

import com.java.koffy.container.Container;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.routing.Router;
import com.java.koffy.server.NativeJettyServer;

public class App {

    private Router router;

    private KoffyRequest request;

    private NativeJettyServer server;

    public static App bootstrap(int port) {
        App app = (App) Container.singleton(App.class);
        app.router = new Router();
        app.server = new NativeJettyServer(port);
        app.request = app.getServer().getRequest();

        return app;
    }

    public Router getRouter() {
        return router;
    }

    public KoffyRequest getRequest() {
        return request;
    }

    public NativeJettyServer getServer() {
        return server;
    }

    public void run() throws Exception {
        server.setRouter(router);
        server.startServer();
    }
}
