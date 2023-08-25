package io.thalys.routing.helpers;

import io.thalys.http.HttpStatus;
import io.thalys.middlewares.Middleware;
import io.thalys.http.RequestEntity;
import io.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware2 implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        return ResponseEntity.textResponse("Stopped", HttpStatus.OK);
    }
}
