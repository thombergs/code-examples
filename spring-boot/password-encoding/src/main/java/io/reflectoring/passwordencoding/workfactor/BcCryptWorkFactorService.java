package io.reflectoring.passwordencoding.workfactor;

import com.google.common.base.Stopwatch;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BcCryptWorkFactorService {

    private static final String TEST_PASSWORD = "my password";
    private static final int GOAL_MILLISECONDS_PER_PASSWORD = 1000;
    private static final int MIN_STRENGTH = 4;
    private static final int MAX_STRENGTH = 31;


    /**
     * Calculates the strength (a.k.a. log rounds) for the BCrypt Algorithm, so that password encoding takes about 1s.
     * This method iterates over strength from 4 to 31 and calculates the duration of password encoding for every value of strength.
     * It returns the first strength, that takes more than 500s
     */
    public int calculateStrength() {
        for (int strength = MIN_STRENGTH; strength <= MAX_STRENGTH; strength++) {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);

            Stopwatch stopwatch = Stopwatch.createStarted();
            bCryptPasswordEncoder.encode(TEST_PASSWORD);
            stopwatch.stop();

            long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (duration >= GOAL_MILLISECONDS_PER_PASSWORD) {
                return strength;
            }
        }
        throw new RuntimeException(String.format("Could not find suitable round number for bcrypt encoding. The encoding with %d rounds" +
                " takes less than %d ms.", MAX_STRENGTH, GOAL_MILLISECONDS_PER_PASSWORD));
    }

    /**
     * Calculates the strength (a.k.a. log rounds) for the BCrypt Algorithm, so that password encoding takes about 1s.
     * This method iterate over strength from 4 to 31 and calculates the duration of password encoding for every value of strength.
     * When the the duration takes more than 1s, it is compared to previous one and the method returns the strength, tha is closer
     * to 1s.
     */
    public int calculateStrengthClosestToTimeGoal() {

        long previousDuration = Long.MIN_VALUE;
        for (int strength = MIN_STRENGTH; strength <= MAX_STRENGTH; strength++) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);

            Stopwatch stopwatch = Stopwatch.createStarted();
            bCryptPasswordEncoder.encode(TEST_PASSWORD);
            stopwatch.stop();
            long currentDuration = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            if (isGreaterThanGoal(currentDuration)) {
                return getStrength(previousDuration, currentDuration, strength);
            }
            previousDuration = currentDuration;
        }
        throw new RuntimeException(String.format("Could not find suitable round number for bcrypt encoding. The encoding with %d rounds" +
                " takes less than %d ms.", MAX_STRENGTH, GOAL_MILLISECONDS_PER_PASSWORD));
    }

    /**
     * @param previousDuration duration from previous iteration
     * @param currentDuration  duration of current iteration
     * @param strength         current strength
     * @return return the current strength, if current duration is closer to GOAL_MILLISECONDS_PER_PASSWORD, otherwise
     * current strength-1.
     */
    int getStrength(long previousDuration, long currentDuration, int strength) {
        if (isPreviousDurationCloserToGoal(previousDuration, currentDuration)) {
            return strength - 1;
        } else {
            return strength;
        }
    }

    private boolean isGreaterThanGoal(long duration) {
        return duration > GOAL_MILLISECONDS_PER_PASSWORD;
    }

    /**
     * return true, if previousDuration is closer to the goal than currentDuration, false otherwise.
     */
    boolean isPreviousDurationCloserToGoal(long previousDuration, long currentDuration) {
        return Math.abs(GOAL_MILLISECONDS_PER_PASSWORD - previousDuration) < Math.abs(GOAL_MILLISECONDS_PER_PASSWORD - currentDuration);
    }
}
