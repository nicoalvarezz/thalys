package com.java.koffy;

import com.java.koffy.autoconfigure.ComponentRegistry;
import com.java.koffy.config.AppConfig;
import com.java.koffy.container.Container;
import com.java.koffy.routing.Router;
import com.java.koffy.server.NativeJettyServer;
import com.java.koffy.session.Session;

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
     * Creates a new instance of the {@link Kinetic} class, sets up a {@link Router}, and a {@link NativeJettyServer}.
     * It creates a singleton of the App class saved it in the singleton {@link Container}.
     *
     * @return The initialized {@link Kinetic} object.
     */
    private static void initialize() {
        router = Container.singleton(Router.class);
        server = new NativeJettyServer();
        server.setPort(appConfig.getAppPort());
        server.setRouter(router);
    }

    public static void start() {
        initialize();
        ComponentRegistry.registerComponents(appConfig.getBasePackage());
        run();
    }

    /**
     * Returns the {@link Session} object initialised when the server starts.
     * @return {@link Session}
     */
    @Deprecated
    public Session session() {
        return server.getSession();
    }

    /**
     * Starts the server on the specified port.
     * Sets the port number and assigns the Router to the NativeJettyServer instance.
     * Calls the startServer() method of the NativeJettyServer to initiate the server startup process.
     *
     * @throws Exception If an exception occurs during server startup.
     */
    private static void run() {
        try {
            server.startServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
