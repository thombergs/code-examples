package io.reflectoring.resilience4j.springboot.services.failures;

public interface PotentialFailure {
    boolean occur();
}