package com.java.koffy.server;

import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;

public interface HttpServer {

    /**
     * Retrieve request the server received.
     * @return {@link RequestEntity}
     *
     */
    RequestEntity getRequest();

    /**
     * Retrieve request the server response.
     * @return {@link ResponseEntity}
     */
    ResponseEntity getResponse();
}
