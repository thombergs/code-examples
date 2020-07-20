package io.reflectoring.resilience4j.retry.exceptions;

public class SeatsUnavailableException extends FlightServiceBaseException {
    public SeatsUnavailableException(String message) {
        super(message);
    }
}