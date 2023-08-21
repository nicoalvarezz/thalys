package com.java.thalys.routing.helpers;

import com.java.thalys.http.HttpStatus;
import com.java.thalys.middlewares.Middleware;
import com.java.thalys.http.RequestEntity;
import com.java.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware2 implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        return ResponseEntity.textResponse("Stopped", HttpStatus.OK);
    }
}
