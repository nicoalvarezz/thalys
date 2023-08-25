package io.thalys.routing.helpers;

import io.thalys.http.Headers.HttpHeader;
import io.thalys.middlewares.Middleware;
import io.thalys.http.RequestEntity;
import io.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        ResponseEntity response = next.apply(request);
        response.setHeader(HttpHeader.SERVER, "fake-test-server");
        return response;
    }
}
