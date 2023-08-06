package com.java.koffy;

import com.java.koffy.autoconfigure.ComponentRegistry;
import com.java.koffy.config.AppConfig;
import com.java.koffy.container.Container;
import com.java.koffy.routing.Router;
import com.java.koffy.server.NativeJettyServer;

/**
 * This class represents the main application class. It serves as the main entry point and contains
 * the logic for initialising and running the framework.
 */
public class Kinetic {

    private static Router router;

    private static NativeJettyServer server;

    private static AppConfig appConfig = new AppConfig();

    /**
     * Initializes and configures the necessary components of the application.
     */
    private static void initialize() {
        router = Container.singleton(Router.class);
        server = new NativeJettyServer();
        server.setPort(appConfig.getAppPort());
    }

    /**
     * Start kinetic application.
     */
    public static void start() {
        initialize();
        ComponentRegistry.registerComponents(appConfig.getBasePackage());
        run();
    }

    /**
     * Run the server.
     *
     * @throws Exception
     */
    private static void run() {
        try {
            server.startServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
