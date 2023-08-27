package com.github.nicoalvarezz.thalys.routing.helpers;

import com.github.nicoalvarezz.thalys.http.HttpStatus;
import com.github.nicoalvarezz.thalys.middlewares.Middleware;
import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;

import java.util.function.Function;

public class MockMiddleware2 implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        return ResponseEntity.textResponse("Stopped", HttpStatus.OK);
    }
}
