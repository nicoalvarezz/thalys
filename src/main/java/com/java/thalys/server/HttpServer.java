package com.java.thalys.server;

import com.java.thalys.http.RequestEntity;
import com.java.thalys.http.ResponseEntity;

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
