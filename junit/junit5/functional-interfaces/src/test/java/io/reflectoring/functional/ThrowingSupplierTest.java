package io.reflectoring.functional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentest4j.AssertionFailedError;

public class ThrowingSupplierTest {
  private final List<Long> numbers = Arrays.asList(100L, 200L, 50L, 300L);
  private final Consumer<List<Long>> checkSorting =
      list -> assertEquals(List.of(50L, 100L, 200L, 300L), list);

  ThrowingSupplier<List<Long>> sorter =
      () -> {
        if (numbers == null || numbers.isEmpty() || numbers.contains(null)) {
          throw new ValidationException("Invalid input");
        }
        TimeUnit.SECONDS.sleep(2);
        return numbers.stream().sorted().toList();
      };

  @ParameterizedTest
  @CsvSource({"25.0d,5.0d", "36.0d,6.0d", "49.0d,7.0d"})
  void testDoesNotThrowWithSupplier(double input, double expected) {
    ThrowingSupplier<Double> findSquareRoot =
        () -> {
          if (input < 0) {
            throw new ValidationException("Invalid input");
          }
          return Math.sqrt(input);
        };
    assertEquals(expected, assertDoesNotThrow(findSquareRoot));
  }

  @Test
  void testAssertTimeoutWithSupplier() {
    // slow execution
    assertThrows(AssertionFailedError.class, () -> assertTimeout(Duration.ofSeconds(1), sorter));

    // fast execution
    assertDoesNotThrow(
        () -> {
          List<Long> result = assertTimeout(Duration.ofSeconds(5), sorter);
          checkSorting.accept(result);
        });

    // reset the number list and verify if the supplier validates it
    Collections.fill(numbers, null);

    ValidationException exception =
        assertThrows(ValidationException.class, () -> assertTimeout(Duration.ofSeconds(1), sorter));
    assertEquals("Invalid input", exception.getMessage());
  }

  @Test
  void testAssertTimeoutPreemptivelyWithSupplier() {
    // slow execution
    assertThrows(
        AssertionFailedError.class, () -> assertTimeoutPreemptively(Duration.ofSeconds(1), sorter));

    // fast execution
    assertDoesNotThrow(
        () -> {
          List<Long> result = assertTimeoutPreemptively(Duration.ofSeconds(5), sorter);
          checkSorting.accept(result);
        });
  }
}
