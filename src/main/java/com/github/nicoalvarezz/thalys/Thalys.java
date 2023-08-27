package com.github.nicoalvarezz.thalys;

import com.github.nicoalvarezz.thalys.autoconfigure.ComponentRegistry;
import com.github.nicoalvarezz.thalys.config.AppConfig;
import com.github.nicoalvarezz.thalys.container.Container;
import com.github.nicoalvarezz.thalys.routing.Router;
import com.github.nicoalvarezz.thalys.server.NativeJettyServer;

/**
 * This class represents the main application class. It serves as the main entry point and contains
 * the logic for initialising and running the framework.
 */
public class Thalys {

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
