package com.java.koffy.autoconfigure;

import com.java.koffy.middlewares.AuthDriver;

public class ClassConfigs {

    Class<? extends AuthDriver> authClazz;

    public void setAuthClazz(Class<? extends AuthDriver> authClazz) {
        this.authClazz = authClazz;
    }

    public AuthDriver getAuthConfig() {
        try {
            return authClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance for " + authClazz.getName(), e);
        }
    }
}
