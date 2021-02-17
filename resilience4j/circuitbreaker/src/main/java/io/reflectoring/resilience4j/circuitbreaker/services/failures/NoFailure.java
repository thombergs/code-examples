package io.reflectoring.resilience4j.circuitbreaker.services.failures;

public class NoFailure implements PotentialFailure {
    @Override
    public void occur() {
    }
}
