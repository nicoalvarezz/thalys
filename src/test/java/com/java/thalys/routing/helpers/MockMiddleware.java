package com.java.thalys.routing.helpers;

import com.java.thalys.http.Headers.HttpHeader;
import com.java.thalys.middlewares.Middleware;
import com.java.thalys.http.RequestEntity;
import com.java.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        ResponseEntity response = next.apply(request);
        response.setHeader(HttpHeader.SERVER, "fake-test-server");
        return response;
    }
}
