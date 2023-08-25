package io.thalys.autoconfigure;

import io.thalys.middlewares.AuthDriver;

/**
 * Class to hold instances of middleware configs created by the client of the framework.
 * This class allows to apply the configurations.
 */
public class MiddlewareConfigs {

    Class<? extends AuthDriver> authClazz;

    /**
     * Set auth config instances.
     *
     * @param authClazz
     */
    public void setAuthClazz(Class<? extends AuthDriver> authClazz) {
        this.authClazz = authClazz;
    }

    /**
     * Get auth config instance.
     *
     * @return {@link AuthDriver}
     */
    public AuthDriver getAuthConfig() {
        try {
            return authClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance for " + authClazz.getName(), e);
        }
    }
}
