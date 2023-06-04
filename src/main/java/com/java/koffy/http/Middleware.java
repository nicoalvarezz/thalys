package com.java.koffy.http;

import java.util.function.Function;

public interface Middleware {

    ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next);
}
