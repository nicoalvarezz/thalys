package com.java.koffy.middlewares;

import com.java.koffy.autoconfigure.ClassConfigs;
import com.java.koffy.container.Container;
import com.java.koffy.exception.MissingConfigException;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;

import java.util.function.Function;


public class Authorization implements Middleware {

    @Override
    public ResponseEntity handle(RequestEntity request, Function<RequestEntity, ResponseEntity> next) {
        if (isAuthorised(request)) {
            return next.apply(request);
        }
        return ResponseEntity.unauthorized();
    }

    private boolean isAuthorised(RequestEntity request) {
        try {
            return Container.resolve(ClassConfigs.class).getAuthConfig().isAuthorized(request);
        } catch (Exception e) {
            throw new MissingConfigException("AuthConfig");
        }
    }
}
