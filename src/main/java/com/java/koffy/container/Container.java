package com.java.koffy.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Container {

    private static Map<String, Object> instances = new HashMap<>();

    public static Object singleton(Class clazz) {
        if (!instances.containsKey(clazz.getName())) {
            try {
                Constructor constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instances.put(clazz.getName(), constructor.newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return instances.get(clazz.getName());
    }

    public static Object resolve(Class<Object> clazz) {
        return instances.get(clazz.getName());
    }
}
