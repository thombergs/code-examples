package io.reflectoring.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ExecutableTest {

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
}
