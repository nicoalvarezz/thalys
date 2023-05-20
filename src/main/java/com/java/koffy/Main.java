package com.java.koffy;

import com.java.koffy.router.Router;

public class Main {

    public static void main(String[] args) throws Exception {

        App app = new App(8080);
        Router router = app.router();

        router.get("/test", () -> {
            int a = 1;
            int b = 2;
            int sum = a + b;
            return "hello world: " + sum;
        });

        app.startServer();
    }
}