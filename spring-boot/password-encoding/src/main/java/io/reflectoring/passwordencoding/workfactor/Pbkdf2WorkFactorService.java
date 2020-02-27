package io.reflectoring.passwordencoding.workfactor;

import com.google.common.base.Stopwatch;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class Pbkdf2WorkFactorService {

  private static final String TEST_PASSWORD = "my password";
  private static final String NO_ADDITIONAL_SECRET = "";
  private static final int GOAL_MILLISECONDS_PER_PASSWORD = 1000;
  private static final int HASH_WIDTH = 256;
  private static final int ITERATION_STEP = 5000;

  /**
   * Finds the number of Iteration for the {@link Pbkdf2PasswordEncoder} to get the duration of
   * password encoding close to 1s. The Calculation does not use any secret (pepper) and applies
   * hash algorithm SHA256.
   */
  public int calculateIteration() {

    int iterationNumber = 150000;
    while (true) {
      Pbkdf2PasswordEncoder pbkdf2PasswordEncoder =
          new Pbkdf2PasswordEncoder(NO_ADDITIONAL_SECRET, iterationNumber, HASH_WIDTH);

      Stopwatch stopwatch = Stopwatch.createStarted();
      pbkdf2PasswordEncoder.encode(TEST_PASSWORD);
      stopwatch.stop();

      long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      if (duration > GOAL_MILLISECONDS_PER_PASSWORD) {
        return iterationNumber;
      }
      iterationNumber += ITERATION_STEP;
    }
  }
}
