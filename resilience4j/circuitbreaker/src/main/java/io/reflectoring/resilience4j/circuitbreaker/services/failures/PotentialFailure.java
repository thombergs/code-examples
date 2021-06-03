package io.reflectoring.resilience4j.circuitbreaker.services.failures;

public interface PotentialFailure {
    void occur();
}