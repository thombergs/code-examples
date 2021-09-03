package io.reflectoring.resilience4j.springboot.services.failures;

public class FailNTimes implements PotentialFailure {
    int times;
    int failedCount;

    public FailNTimes(int times) {
        this.times = times;
    }

    @Override
    public boolean occur() {
        if (failedCount++ < times) {
            System.out.println("Operation failed");
            throw new RuntimeException("Operation failed");
        }
        return false;
    }
}