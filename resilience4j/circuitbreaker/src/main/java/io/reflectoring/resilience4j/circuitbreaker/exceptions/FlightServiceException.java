package io.reflectoring.resilience4j.circuitbreaker.exceptions;

public class FlightServiceException extends RuntimeException {
    public FlightServiceException(String message) {
        super(message);
    }
}