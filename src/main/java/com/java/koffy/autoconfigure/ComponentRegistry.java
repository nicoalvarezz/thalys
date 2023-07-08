package com.java.koffy.autoconfigure;

import com.java.koffy.annotation.DeletePath;
import com.java.koffy.annotation.GetPath;
import com.java.koffy.annotation.PostPath;
import com.java.koffy.annotation.PutPath;
import com.java.koffy.annotation.RequestPath;
import com.java.koffy.annotation.RestController;
import com.java.koffy.container.Container;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.routing.Router;

import java.util.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ComponentRegistry {

    public static void registerComponents(String basePackage) {
        registerRoutes(basePackage);
    }

    private static void registerRoutes(String basePackage) {
        List<Class<?>> annotatedBeans = ClassPathScanner.scanClasses(basePackage, RestController.class);
        MethodWrapper methodWrapper = new MethodWrapper();

        for (Class<?> clazz : annotatedBeans) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                try {
                    Function<RequestEntity, ResponseEntity> action =
                            methodWrapper.asFunction(method, clazz.getDeclaredConstructor().newInstance());
                    routerHandler(clazz, method, action);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("Failed to create instance for " + clazz.getName(), e);
                }
            }
        }
    }

    private static void routerHandler(Class<?> clazz, Method method, Function<RequestEntity, ResponseEntity> action) {
        Router router = Container.singleton(Router.class);

        if (method.isAnnotationPresent(GetPath.class)) {
            router.get(requestPath(clazz) + method.getAnnotation(GetPath.class).value(), action);
        } else if (method.isAnnotationPresent(PostPath.class)) {
            router.post(requestPath(clazz) + method.getAnnotation(PostPath.class).value(), action);
        } else if (method.isAnnotationPresent(PutPath.class)) {
            router.put(requestPath(clazz) + method.getAnnotation(PutPath.class).value(), action);
        } else if (method.isAnnotationPresent(DeletePath.class)) {
            router.delete(requestPath(clazz) + method.getAnnotation(DeletePath.class).value(), action);
        }
    }

    private static String requestPath(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequestPath.class)) {
            return clazz.getAnnotation(RequestPath.class).value();
        }
        return "";
    }
}
