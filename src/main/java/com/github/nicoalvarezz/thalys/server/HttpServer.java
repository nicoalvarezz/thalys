package com.github.nicoalvarezz.thalys.server;

import com.github.nicoalvarezz.thalys.http.RequestEntity;
import com.github.nicoalvarezz.thalys.http.ResponseEntity;

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
