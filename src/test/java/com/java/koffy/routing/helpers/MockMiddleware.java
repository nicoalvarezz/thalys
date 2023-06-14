package com.java.koffy.routing.helpers;

import com.java.koffy.http.Headers.HttpHeader;
import com.java.koffy.http.Middleware;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        ResponseEntity response = next.apply(request);
        response.setHeader(HttpHeader.SERVER.get(), "fake-test-server");
        return response;
    }
}
