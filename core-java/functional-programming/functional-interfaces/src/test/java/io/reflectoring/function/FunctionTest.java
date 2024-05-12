package io.reflectoring.function;

import java.util.function.*;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunctionTest {

  @Test
  void simpleFunction() {
    Function<String, String> toUpper = s -> s == null ? null : s.toUpperCase();
    Assertions.assertEquals("JOY", toUpper.apply("joy"));
    Assertions.assertNull(toUpper.apply(null));
  }

  @Test
  void functionComposition() {
    Function<String, String> toUpper = s -> s == null ? null : s.toUpperCase();
    Function<String, String> replaceVowels =
        s ->
            s == null
                ? null
                : s.replace("A", "")
                    .replace("E", "")
                    .replace("I", "")
                    .replace("O", "")
                    .replace("U", "");
    Assertions.assertEquals("APPLE", toUpper.compose(replaceVowels).apply("apple"));
    Assertions.assertEquals("PPL", toUpper.andThen(replaceVowels).apply("apple"));
  }

  @Test
  void biFunction() {
    BiFunction<Integer, Integer, Integer> bigger =
        (first, second) -> first > second ? first : second;
    Function<Integer, Integer> square = number -> number * number;

    Assertions.assertEquals(10, bigger.apply(4, 10));
    Assertions.assertEquals(100, bigger.andThen(square).apply(4, 10));
  }

  @Test
  void intFunction() {
    IntFunction<Integer> square = number -> number * number;
    Assertions.assertEquals(100, square.apply(10));
  }

  @Test
  void intToDoubleFunction() {
    int principalAmount = 1000; // Initial investment amount
    double interestRate = 0.05; // Annual accruedInterest rate (5%)

    IntToDoubleFunction accruedInterest = principal -> principal * interestRate;
    Assertions.assertEquals(50.0, accruedInterest.applyAsDouble(principalAmount));
  }

  @Test
  void intToLongFunction() {
    IntToLongFunction factorial =
        n -> {
          long result = 1L;
          for (int i = 1; i <= n; i++) {
            result *= i;
          }
          return result;
        };
    IntStream input = IntStream.range(1, 6);
    final long[] result = input.mapToLong(factorial).toArray();
    Assertions.assertArrayEquals(new long[] {1L, 2L, 6L, 24L, 120L}, result);
  }
}
