package com.java.koffy;

import com.java.koffy.http.Header;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.http.Middleware;

import java.util.HashMap;
import java.util.function.Function;

public class AuthMiddleware implements Middleware {

    public AuthMiddleware() {

    }

    @Override
    public KoffyResponse handle(KoffyRequest request, Function<KoffyRequest, KoffyResponse> next) {
        if (request.getHeader(Header.AUTHORIZATION).isEmpty() ||
                !request.getHeader(Header.AUTHORIZATION).get().equals("test")) {
            return KoffyResponse.jsonResponse(new HashMap<>() {{
                put("message", "Not authenticated");
            }}).build();
        }
        return next.apply(request);
    }
}
