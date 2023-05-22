package com.java.koffy;

import com.java.koffy.Server.NativeJettyServer;
import com.java.koffy.http.Response;
import com.java.koffy.routing.Router;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws Exception {
        Router newRouter = new Router();

        newRouter.get("/test", () -> {
            Response response = new Response();
            response.setHeader("Content-Type", "application/json");
            response.setContent(new JSONObject().put("message", "GET OK").toString());
            return response;
        });

        newRouter.post("/test", Response::new);

        NativeJettyServer server = new NativeJettyServer(8080);
        server.setRouter(newRouter);
        server.startServer();
    }
}