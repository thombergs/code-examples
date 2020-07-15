package io.reflectoring.resilience4j.retry.exceptions;

public class FlightServiceBaseException extends RuntimeException {
    public FlightServiceBaseException(String message) {
        super(message);
    }
}