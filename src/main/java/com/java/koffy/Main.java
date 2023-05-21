package com.java.koffy;

import com.java.koffy.Server.Server;
import com.java.koffy.router.Router;

public class Main {

    public static void main(String[] args) throws Exception {
        Router newRouter = new Router();

        newRouter.get("/test", () -> {
            int a = 1;
            int b = 2;
            int sum = a + b;
            return "hello world: " + sum;
        });

        newRouter.post("/test", () -> "POST OK");

        Server server = new Server(8080);
        server.setRouter(newRouter);
        server.startServer();
    }
}