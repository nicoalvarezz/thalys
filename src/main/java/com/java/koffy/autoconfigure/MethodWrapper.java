package com.java.koffy.autoconfigure;

import java.util.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper {

    public <T, R> Function<T, R> asFunction(Method method, Object instance) {
        return argument -> {
            try {
                return (R) method.invoke(instance, argument);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke method", e);
            }
        };
    }
}
