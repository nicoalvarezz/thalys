package com.github.nicoalvarezz.thalys.middlewares;

import com.github.nicoalvarezz.thalys.http.RequestEntity;

/**
 * An interface that must be implemented to configure authentication middleware settings.
 * This interface defines methods that allow for the setup and customization
 * of authentication mechanisms within the framework.
 */
public interface AuthDriver {

    boolean isAuthorized(RequestEntity request);
}
