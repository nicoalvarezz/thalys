package com.java.koffy.http;

import java.util.function.Function;

public interface Middleware {

    KoffyResponse handle(KoffyRequest request, Function<KoffyRequest, KoffyResponse> next);
}
