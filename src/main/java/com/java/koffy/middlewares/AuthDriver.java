package com.java.koffy.middlewares;

import com.java.koffy.http.RequestEntity;

public interface AuthDriver {

    boolean isAuthorized(RequestEntity request);
}
