package com.java.koffy.http;

import java.util.function.Supplier;

public interface Middleware {

    KoffyResponse handle(KoffyRequest request, Supplier<KoffyResponse> next);
}
