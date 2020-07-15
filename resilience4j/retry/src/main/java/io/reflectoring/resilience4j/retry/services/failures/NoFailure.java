package io.reflectoring.resilience4j.retry.services.failures;

public class NoFailure implements PotentialFailure {
    @Override
    public void occur() {
    }
}
