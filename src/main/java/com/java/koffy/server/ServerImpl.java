package com.java.koffy.server;

import com.java.koffy.http.KoffyRequest;
import com.java.koffy.http.KoffyResponse;

public interface ServerImpl {

    /**
     * Retrieve request the server received.
     * @return {@link KoffyRequest}
     *
     */
    KoffyRequest getRequest();

    /**
     * Retrieve request the server response.
     * @return {@link KoffyResponse}
     */
    KoffyResponse getResponse();
}
