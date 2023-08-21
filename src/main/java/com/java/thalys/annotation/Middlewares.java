package com.java.thalys.annotation;

import com.java.thalys.middlewares.Middleware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Middlewares {
    Class<? extends Middleware>[] value();
}
