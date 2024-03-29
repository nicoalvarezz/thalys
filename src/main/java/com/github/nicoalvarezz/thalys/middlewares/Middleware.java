package com.github.nicoalvarezz.thalys.middlewares;

import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;

import java.util.function.Function;

/**
 * The `Middleware` interface defines the contract that middleware components in the framework that
 * must adhere to. Middleware components implement this interface to provide custom logic that can be
 * inserted into the request-response pipeline to intercept, modify, or enhance incoming requests and
 * outgoing responses.
 */
public interface Middleware {

    ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next);
}
