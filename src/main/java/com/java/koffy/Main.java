package com.java.koffy;

import com.java.koffy.http.KoffyResponse;

public class Main {

    public static void main(String[] args) throws Exception {
        App app = App.bootstrap();
        app.router().get("/test/{param}", (request) -> KoffyResponse.jsonResponse(request.routeParams())
                .status(200)
                .build());

        app.router().post("/test-post", (request) ->
                KoffyResponse.jsonResponse(request.getPostData()).status(200).build());

        app.router().get("/test", (request) ->
            KoffyResponse.jsonResponse(request.getQueryData()).status(200).build());


        app.router().get("/redirect", (request) -> KoffyResponse.redirectResponse("/test/5"));

//        app.router().get("/middlewares", (request -> KoffyResponse.jsonResponse(new HashMap<>() {{
//            put("message", "OK");
//        }}).build())).setMiddlewares(new ArrayList<>() {{
//            add(AuthMiddleware.class);
//        }});

        app.run(8000);
    }
}

