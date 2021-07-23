package io.reflectoring.resilience4j.springboot.services.failures;

import io.reflectoring.resilience4j.springboot.exceptions.RateLimitExceededException;

public class RateLimitFailNTimes implements PotentialFailure {
    int times;
    int failedCount;

    public RateLimitFailNTimes(int times) {
        this.times = times;
    }

    @Override
    public boolean occur() {
        if (failedCount++ < times) {
            System.out.println("Rate limit exceeded");
            throw new RateLimitExceededException("Rate limit exceeded, try again in some time", "RL-101");
        }
        return false;
    }
}
