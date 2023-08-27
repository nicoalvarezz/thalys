package com.github.nicoalvarezz.thalys.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container class that allows storing and retrieving singleton instances.
 */
public class Container {

    /**
     * Map containing instances of singletons. It uses {@link ConcurrentHashMap} to ensure thread safety.
     */
    private static final Map<Class<?>, Object> INSTANCES = new ConcurrentHashMap<>();

    /**
     * Returns a single instance of the specified class. If an instance of the class does not exist in the container,
     * it is created using reflection and stored in the container for future use.
     * Subsequent calls to singleton for the same class will return the existing instance.
     * @param clazz specified class
     * @return {@link T} instance of specified class
     * @throws RuntimeException Failure to create instance
     */
    public static <T> T singleton(Class<T> clazz) {
        return clazz.cast(INSTANCES.computeIfAbsent(clazz, key -> {
            try {
                return key.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance for " + key.getName(), e);
            }
        }));
    }

    /**
     * This method is used to retrieve a singleton instance of the specified class.
     * @param clazz specified instance
     * @return {@link T} instance of specified class
     */
    public static <T> T resolve(Class<T> clazz) {
        return clazz.cast(INSTANCES.get(clazz));
    }
}
