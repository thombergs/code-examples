package io.reflectoring.resilience4j.springboot.exceptions;

public class FlightServiceBaseException extends RuntimeException {
    public FlightServiceBaseException(String message) {
        super(message);
    }
}