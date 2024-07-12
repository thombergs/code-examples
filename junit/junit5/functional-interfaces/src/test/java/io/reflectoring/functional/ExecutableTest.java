package io.reflectoring.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentest4j.AssertionFailedError;

public class ExecutableTest {
  private final List<Long> numbers = Arrays.asList(100L, 200L, 50L, 300L);
  final Executable sorter =
      () -> {
        TimeUnit.SECONDS.sleep(2);
        numbers.sort(Long::compareTo);
      };
  private final Executable checkSorting =
      () -> assertEquals(List.of(50L, 100L, 200L, 300L), numbers);
  private final Executable noChanges = () -> assertEquals(List.of(100L, 200L, 50L, 300L), numbers);

  @ParameterizedTest
  @CsvSource({"1,1,2,Hello,H,bye,2,byebye", "4,5,9,Good,Go,Go,-10,", "10,21,31,Team,Tea,Stop,-2,"})
  void testAssertAllWithExecutable(
      int num1,
      int num2,
      int sum,
      String input,
      String prefix,
      String arg,
      int count,
      String result) {
    assertAll(
        () -> assertEquals(sum, num1 + num2),
        () -> assertTrue(input.startsWith(prefix)),
        () -> {
          if (count < 0) {
            assertThrows(
                IllegalArgumentException.class,
                () -> {
                  new ArrayList<>(count);
                });
          } else {
            assertEquals(result, arg.repeat(count));
          }
        });
  }

  @ParameterizedTest
  @CsvSource({"one,0,o", "one,1,n"})
  void testAssertDoesNotThrowWithExecutable(String input, int index, char result) {
    assertDoesNotThrow(() -> assertEquals(input.charAt(index), result));
  }

  @Test
  void testAssertThrowsWithExecutable() {
    List<String> input = Arrays.asList("one", "", "three", null, "five");
    final IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              for (String value : input) {
                if (value == null || value.isBlank()) {
                  throw new IllegalArgumentException("Got invalid value");
                }
                // process values
              }
            });
    assertEquals("Got invalid value", exception.getMessage());
  }

  @Test
  void testAssertTimeoutWithExecutable() {
    assertAll(
        () ->
            assertThrows(
                AssertionFailedError.class, () -> assertTimeout(Duration.ofSeconds(1), sorter)),
        checkSorting);

    assertAll(
        () -> assertDoesNotThrow(() -> assertTimeout(Duration.ofSeconds(5), sorter)), checkSorting);
  }

  @Test
  void testAssertTimeoutPreemptivelyWithExecutable() {
    assertAll(
        () ->
            assertThrows(
                AssertionFailedError.class,
                () -> assertTimeoutPreemptively(Duration.ofSeconds(1), sorter)),
        noChanges);

    assertAll(
        () -> assertDoesNotThrow(() -> assertTimeoutPreemptively(Duration.ofSeconds(5), sorter)),
        checkSorting);
  }
}
