package com.java.koffy;

import com.java.koffy.http.Header;
import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;
import com.java.koffy.http.Middleware;

import java.util.HashMap;
import java.util.function.Supplier;

public class AuthMiddleware implements Middleware {

    public AuthMiddleware() {

    }

    @Override
    public KoffyResponse handle(KoffyRequest request, Supplier<KoffyResponse> next) {
        if (request.getHeaders(Header.AUTHORIZATION).isEmpty() ||
                !request.getHeaders(Header.AUTHORIZATION).get().equals("test")) {
            return KoffyResponse.jsonResponse(401, new HashMap<>() {{
                put("message", "Not authenticated");
            }});
        }
        return next.get();
    }
}
