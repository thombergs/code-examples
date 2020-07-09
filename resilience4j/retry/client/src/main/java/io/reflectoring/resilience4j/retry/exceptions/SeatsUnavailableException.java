package io.reflectoring.resilience4j.retry.exceptions;

public class SeatsUnavailableException extends RuntimeException {
    public SeatsUnavailableException(String message) {
        super(message);
    }
}
