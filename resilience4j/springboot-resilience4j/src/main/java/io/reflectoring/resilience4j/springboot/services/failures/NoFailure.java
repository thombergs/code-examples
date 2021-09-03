package io.reflectoring.resilience4j.springboot.services.failures;

public class NoFailure implements PotentialFailure {
    @Override
    public boolean occur() {
        return false;
    }
}
