package com.java.thalys.autoconfigure;

import java.util.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The MethodWrapper class provides a utility for converting a Java method into a functional interface.
 * This allows a method to be invoked and treated as a functional object, making it easier to work with.
 * Allows to work with registered endpoint methods with the format that the framework uses internally
 */
public class MethodWrapper {

    /**
     * Converts a Java method into a functional interface that can be invoked with an argument.
     *
     * @param method
     * @param instance
     * @return A functional interface representing the method invocation
     */
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
