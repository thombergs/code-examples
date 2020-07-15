package io.reflectoring.resilience4j.retry.services.failures;

import io.reflectoring.resilience4j.retry.exceptions.SeatsUnavailableException;

public class SeatsUnavailableFailureNTimes implements PotentialFailure {
    int times;
    int failedCount;

    public SeatsUnavailableFailureNTimes(int times) {
        this.times = times;
    }

    @Override
    public void occur() {
        if (failedCount++ < times) {
            System.out.println("Seats not available");
            throw new SeatsUnavailableException("Seats not available");
        }
    }
}
