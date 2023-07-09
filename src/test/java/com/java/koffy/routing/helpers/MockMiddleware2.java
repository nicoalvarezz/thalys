package com.java.koffy.routing.helpers;

import com.java.koffy.http.HttpStatus;
import com.java.koffy.http.Middleware;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware2 implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        return ResponseEntity.textResponse("Stopped", HttpStatus.OK);
    }
}
