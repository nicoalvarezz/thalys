package com.java.koffy;

import com.java.koffy.config.DatabaseConfig;
import com.java.koffy.container.Container;
import com.java.koffy.database.DatabaseConnection;
import com.java.koffy.routing.Router;
import com.java.koffy.server.NativeJettyServer;
import com.java.koffy.session.Session;

/**
 * This class represents the main application class. It serves as the main entry point and contains
 * the logic for initialising and running the framework.
 */
public class App {

    private Router router;

    private NativeJettyServer server;

    private DatabaseConnection databaseConnection;

    private static DatabaseConfig databaseConfig = new DatabaseConfig();

    /**
     * Initializes and configures the necessary components of the application.
     * Creates a new instance of the {@link App} class, sets up a {@link Router}, and a {@link NativeJettyServer}.
     * It creates a singleton of the App class saved it in the singleton {@link Container}.
     *
     * @return The initialized {@link App} object.
     */
    public static App bootstrap() {
        App app = Container.singleton(App.class);
        app.router = new Router();
        app.server = new NativeJettyServer();
        app.databaseConnection = Container.singleton(DatabaseConnection.class);
        app.databaseConnection
                .connect(databaseConfig.datasourceUrl(), databaseConfig.username(), databaseConfig.password());
        return app;
    }

    /**
     * Returns the {@link Router} object associated with the {@link App} instance.
     *
     * @return The {@link Router} object.
     */
    public Router router() {
        return router;
    }

    /**
     * Returns the {@link Session} object initialised when the server starts.
     * @return {@link Session}
     */
    public Session session() {
        return server.getSession();
    }

    /**
     * Starts the server on the specified port.
     * Sets the port number and assigns the Router to the NativeJettyServer instance.
     * Calls the startServer() method of the NativeJettyServer to initiate the server startup process.
     *
     * @param port The port number on which to start the server.
     * @throws Exception If an exception occurs during server startup.
     */
    public void run(int port) throws Exception {
        server.setPort(port);
        server.setRouter(router);
        server.startServer();
    }
}
