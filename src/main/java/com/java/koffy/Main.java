package com.java.koffy;

import com.java.koffy.http.ResponseEntity;

public class Main {

    public static void main(String[] args) throws Exception {
        App app = App.bootstrap();
        app.router().get("/test/{param}", (request) -> ResponseEntity.jsonResponse(request.routeParams())
                .status(200)
                .build());

        app.router().post("/test-post", (request) ->
                ResponseEntity.jsonResponse(request.getPostData()).status(200).build());

        app.router().get("/test", (request) ->
            ResponseEntity.jsonResponse(request.getQueryData()).status(200).build());

        app.router().get("/redirect", (request) -> ResponseEntity.redirectResponse("/test/5"));

//        app.router().get("/middlewares", (request -> ResponseEntity.jsonResponse(new HashMap<>() {{
//            put("message", "OK");
//        }}).build())).setMiddlewares(new ArrayList<>() {{
//            add(AuthMiddleware.class);
//        }});

        app.run(8000);
    }
}

