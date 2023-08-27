package com.github.nicoalvarezz.thalys.routing.helpers;

import com.github.nicoalvarezz.thalys.middlewares.Middleware;
import com.github.nicoalvarezz.thalys.http.Headers.HttpHeader;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        ResponseEntity response = next.apply(request);
        response.setHeader(HttpHeader.SERVER, "fake-test-server");
        return response;
    }
}
