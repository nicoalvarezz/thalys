package com.java.thalys.autoconfigure;

import com.java.thalys.annotation.Configurable;
import com.java.thalys.annotation.DeletePath;
import com.java.thalys.annotation.GetPath;
import com.java.thalys.annotation.Middlewares;
import com.java.thalys.annotation.PatchPath;
import com.java.thalys.annotation.PostPath;
import com.java.thalys.annotation.PutPath;
import com.java.thalys.annotation.RequestPath;
import com.java.thalys.annotation.RestController;
import com.java.thalys.container.Container;
import com.java.thalys.http.RequestEntity;
import com.java.thalys.http.ResponseEntity;
import com.java.thalys.middlewares.AuthDriver;
import com.java.thalys.middlewares.Middleware;
import com.java.thalys.routing.Route;
import com.java.thalys.routing.Router;

import java.util.Arrays;
import java.util.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Class responsible for registering different components within the framework.
 */
public class ComponentRegistry {

    private static MiddlewareConfigs clazzConfigs = Container.singleton(MiddlewareConfigs.class);

    private static MethodWrapper methodWrapper = new MethodWrapper();

    private static final Router ROUTER = Container.resolve(Router.class);

    /**
     * Register components by scanning the provided base packages.
     *
     * @param basePackage
     */
    public static void registerComponents(String basePackage) {
        registerRoutes(basePackage);
        registerMiddlewareConfigs(basePackage);
    }

    private static void registerRoutes(String basePackage) {
        List<Class<?>> annotatedBeans = ClassPathScanner
                .scanClassesForGivenAnnotation(basePackage, RestController.class);

        for (Class<?> clazz : annotatedBeans) {
            registerMethods(clazz, clazz.getDeclaredMethods());
        }
    }

    private static void registerMethods(Class<?> clazz, Method[] methods) {
        for (Method method : methods) {
            try {
                Function<RequestEntity, ResponseEntity> action =
                        methodWrapper.asFunction(method, clazz.getDeclaredConstructor().newInstance());
                routerHandler(clazz, method, action);
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Failed to create instance for " + clazz.getName(), e);
            }
        }
    }

    private static void routerHandler(Class<?> clazz, Method method, Function<RequestEntity, ResponseEntity> action) {
        Route route = new Route();

        if (method.isAnnotationPresent(GetPath.class)) {
            route = ROUTER.get(requestPath(clazz) + method.getAnnotation(GetPath.class).value(), action);
        } else if (method.isAnnotationPresent(PostPath.class)) {
            route = ROUTER.post(requestPath(clazz) + method.getAnnotation(PostPath.class).value(), action);
        } else if (method.isAnnotationPresent(PutPath.class)) {
            route = ROUTER.put(requestPath(clazz) + method.getAnnotation(PutPath.class).value(), action);
        } else if (method.isAnnotationPresent(PatchPath.class)) {
            route = ROUTER.patch(requestPath(clazz) + method.getAnnotation(PatchPath.class).value(), action);
        } else if (method.isAnnotationPresent(DeletePath.class)) {
            route = ROUTER.delete(requestPath(clazz) + method.getAnnotation(DeletePath.class).value(), action);
        }

        if (method.isAnnotationPresent(Middlewares.class)) {
            registerMiddlewares(route, Arrays.stream(method.getAnnotation(Middlewares.class).value()).toList());
        }
    }

    private static String requestPath(Class<?> clazz) {
        return clazz.isAnnotationPresent(RequestPath.class) ? clazz.getAnnotation(RequestPath.class).value() : "";
    }

    private static void registerMiddlewares(Route route, List<Class<? extends Middleware>> middlewares) {
        route.setMiddlewares(middlewares);
    }

    private static void registerMiddlewareConfigs(String basePackage) {
        List<Class<?>> annotatedBeans = ClassPathScanner.scanClassesForGivenAnnotation(basePackage, Configurable.class);

        for (Class<?> clazz : annotatedBeans) {
            registerAuthMiddlewareConfigs(clazz);
        }
    }

    private static void registerAuthMiddlewareConfigs(Class<?> clazz) {
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze == AuthDriver.class) {
                clazzConfigs.setAuthClazz((Class<? extends AuthDriver>) clazz);
            }
        }
    }
}











//HOLAAAAAAAAA TONTORROOOOOON
//TE ESCRIBO ESTO PARA QUE TE ACUERDES DE MI
