package com.java.koffy;

import com.java.koffy.container.Container;
import com.java.koffy.routing.Router;
import com.java.koffy.server.NativeJettyServer;

public class App {

    private Router router;

    private NativeJettyServer server;

    public static App bootstrap() {
        App app = Container.singleton(App.class);
        app.router = new Router();
        app.server = new NativeJettyServer();

        return app;
    }

    public Router router() {
        return router;
    }

    public void run(int port) throws Exception {
        server.setPort(port);
        server.setRouter(router);
        server.startServer();
    }
}
