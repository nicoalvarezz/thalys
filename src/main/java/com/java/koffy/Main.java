package com.java.koffy;

import com.java.koffy.http.Header;
import com.java.koffy.http.KoffyResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        App app = App.bootstrap();
        app.router().get("/test/{param}", (request) -> KoffyResponse.jsonResponse(request.routeParams())
                .status(200)
                .header(Header.AUTHORIZATION, "test")
                .build());

        app.router().post("/test", (request) ->
                KoffyResponse.textResponse("POST OK").status(200).build());

        app.router().get("/redirect", (request) -> KoffyResponse.redirectResponse("/test/5"));

        app.router().get("/middlewares", (request -> KoffyResponse.jsonResponse(new HashMap<>() {{
            put("message", "OK");
        }}).build())).setMiddlewares(new ArrayList<>() {{
            add(AuthMiddleware.class);
        }});

        app.run(8000);
    }
}

