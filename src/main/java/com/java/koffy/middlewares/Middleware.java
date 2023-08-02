package com.java.koffy.middlewares;

import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;

import java.util.function.Function;

public interface Middleware {

    ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next);
}
