package com.java.koffy;

import com.java.koffy.http.KoffyResponse;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        App app = App.bootstrap();
        app.router().get("/test/{param}", (request) -> KoffyResponse.jsonResponse(200, request.routeParams()));

        app.router().post("/test", (request) ->
                KoffyResponse.jsonResponse(200, new HashMap<>() {{ put("message", "POST OK"); }}));

        app.router().get("/redirect", (request) -> KoffyResponse.redirectResponse("/test/5"));

        app.run(8000);
    }
}
