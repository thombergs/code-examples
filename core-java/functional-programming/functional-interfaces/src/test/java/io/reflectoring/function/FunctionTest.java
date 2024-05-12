package io.reflectoring.function;

import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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

  @Test
  void longFunction() {
    LongFunction<Double> squareArea = side -> (double) (side * side);
    Assertions.assertEquals(400d, squareArea.apply(20L));
  }

  @Test
  void longToDoubleFunction() {
    LongToDoubleFunction squareArea = side -> (double) (side * side);
    Assertions.assertEquals(400d, squareArea.applyAsDouble(20L));

    LongStream input = LongStream.range(1L, 6L);
    final double[] result = input.mapToDouble(squareArea).toArray();
    Assertions.assertArrayEquals(new double[] {1.0, 4.0, 9.0, 16.0, 25.0}, result);
  }

  @Test
  void longToIntFunction() {
    LongToIntFunction digitCount = number -> String.valueOf(number).length();
    LongStream input = LongStream.of(1L, 120, 15L, 12345L);
    final int[] result = input.mapToInt(digitCount).toArray();
    Assertions.assertArrayEquals(new int[] {1, 3, 2, 5}, result);
  }
}
