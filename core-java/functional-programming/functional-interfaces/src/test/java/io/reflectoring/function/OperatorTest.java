package io.reflectoring.function;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OperatorTest {
  @Test
  void unaryOperator() {
    UnaryOperator<String> trim = value -> value == null ? null : value.trim();
    UnaryOperator<String> upperCase = value -> value == null ? null : value.toUpperCase();
    Function<String, String> transform = trim.andThen(upperCase);

    Assertions.assertEquals("joy", trim.apply("  joy "));
    Assertions.assertEquals("  JOY ", upperCase.apply("  joy "));
    Assertions.assertEquals("JOY", transform.apply("  joy "));
  }

  @Test
  void intUnaryOperator() {
    // formula y = x^2 + 2x + 1
    IntUnaryOperator formula = x -> (x * x) + (2 * x) + 1;
    Assertions.assertEquals(36, formula.applyAsInt(5));

    IntStream input = IntStream.of(2, 3, 4);
    final int[] result = input.map(formula).toArray();
    Assertions.assertArrayEquals(new int[] {9, 16, 25}, result);

    // the population doubling every 3 years, one fifth migrate and 10% mortality
    IntUnaryOperator growth = number -> number * 2;
    IntUnaryOperator migration = number -> number * 4 / 5;
    IntUnaryOperator mortality = number -> number * 9 / 10;
    IntUnaryOperator population = growth.andThen(migration).andThen(mortality);
    Assertions.assertEquals(1440000, population.applyAsInt(1000000));
  }

  @Test
  void longUnaryOperator() {
    // light travels 186282 miles per seconds
    LongUnaryOperator distance = time -> time * 186282;
    // denser medium slows light down
    LongUnaryOperator slowDown = dist -> dist * 2 / 3;
    LongUnaryOperator actualDistance = distance.andThen(slowDown);

    Assertions.assertEquals(931410, distance.applyAsLong(5));
    Assertions.assertEquals(620940, actualDistance.applyAsLong(5));

    final LongStream input = LongStream.of(5, 10, 15);
    final long[] result = input.map(distance).toArray();
    Assertions.assertArrayEquals(new long[] {931410L, 1862820L, 2794230L}, result);
  }
}
