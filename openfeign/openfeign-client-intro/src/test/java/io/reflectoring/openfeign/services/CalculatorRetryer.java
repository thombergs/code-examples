package io.reflectoring.openfeign.services;

import feign.RetryableException;
import feign.Retryer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class CalculatorRetryer implements Retryer {
    /**
     * millis to wait between retries
     */
    private final long period;

    /**
     * Maximum number of retries
     */
    private final int maxAttempts;

    private int attempt = 1;

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.info("Feign retry attempt {} of {} due to {} ", attempt, maxAttempts, e.getMessage());
        if (++attempt > maxAttempts) {
            throw e;
        }
        if (e.status() == 401) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ex) {
                throw e;
            }
        } else {
            throw e;
        }
    }

    @Override
    public Retryer clone() {
        return this;
    }

    public int getRetryAttempts() {
        // return this instead of creating a new instance, as it is a stateful retryer... it has to return the maxAttempts.
        return attempt - 1; // Subtract 1 to exclude the initial attempt
    }
}
