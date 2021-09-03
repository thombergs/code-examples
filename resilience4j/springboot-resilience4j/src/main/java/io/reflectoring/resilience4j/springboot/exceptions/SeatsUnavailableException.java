package io.reflectoring.resilience4j.springboot.exceptions;

public class SeatsUnavailableException extends FlightServiceBaseException {
    public SeatsUnavailableException(String message) {
        super(message);
    }
}