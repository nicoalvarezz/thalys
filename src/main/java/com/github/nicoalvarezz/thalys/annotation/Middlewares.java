package com.github.nicoalvarezz.thalys.annotation;

import com.github.nicoalvarezz.thalys.middlewares.Middleware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Middlewares {
    Class<? extends Middleware>[] value();
}
