package com.java.koffy;

import com.java.koffy.Server.NativeJettyServer;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.routing.Router;

public class Main {

    public static void main(String[] args) throws Exception {
        Router newRouter = new Router();

        newRouter.get("/test", () -> KoffyResponse.textResponse(200, "GET OK"));

        newRouter.post("/test", () -> KoffyResponse.textResponse(200, "POST OK"));

        newRouter.get("/redirect", () -> KoffyResponse.redirectResponse("/test"));

        NativeJettyServer server = new NativeJettyServer(8080);
        server.setRouter(newRouter);
        server.startServer();
    }
}