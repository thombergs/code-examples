package io.reflectoring.resilience4j.retry.services.failures;

public interface PotentialFailure {
    void occur();
}