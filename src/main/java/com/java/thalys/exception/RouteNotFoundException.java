package com.java.thalys.exception;

/**
 * The RouteNotFoundException class represents an exception that is thrown when a requested route
 * cannot be found within the Koffy framework. This exception is a subtype of the RuntimeException
 * class, making it an unchecked exception.
 */
public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(String message) {
        super(message);
    }
}
