package io.reflectoring.function;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.reflectoring.function.custom.ArithmeticOperation;
import jakarta.validation.constraints.NotNull;

public class OperatorTest {
  @Test
  void unaryOperator() {
    ArithmeticOperation add = (var a, var b) -> a + b;
    ArithmeticOperation addNullSafe = (@NotNull var a, @NotNull var b) -> a + b;
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

  @Test
  void doubleUnaryOperator() {
    DoubleUnaryOperator circleArea = radius -> radius * radius * Math.PI;
    DoubleUnaryOperator doubleIt = area -> area * 4;
    DoubleUnaryOperator scaling = circleArea.andThen(doubleIt);

    Assertions.assertEquals(153.93D, circleArea.applyAsDouble(7), 0.01);
    Assertions.assertEquals(615.75D, scaling.applyAsDouble(7), 0.01);

    final DoubleStream input = DoubleStream.of(7d, 14d, 21d);
    final double[] result = input.map(circleArea).toArray();
    Assertions.assertArrayEquals(new double[] {153.93D, 615.75D, 1385.44D}, result, 0.01);
  }

  @Test
  void binaryOperator() {
    LongUnaryOperator factorial =
        n -> {
          long result = 1L;
          for (int i = 1; i <= n; i++) {
            result *= i;
          }
          return result;
        };
    // Calculate permutations
    BinaryOperator<Long> npr = (n, r) -> factorial.applyAsLong(n) / factorial.applyAsLong(n - r);
    // Verify permutations
    // 3P2 means the number of permutations of 2 that can be achieved from a choice of 3.
    final Long result3P2 = npr.apply(3L, 2L);
    Assertions.assertEquals(6L, result3P2);

    // Add two prices
    BinaryOperator<Double> addPrices = Double::sum;
    // Apply discount
    UnaryOperator<Double> applyDiscount = total -> total * 0.9; // 10% discount
    // Apply tax
    UnaryOperator<Double> applyTax = total -> total * 1.07; // 7% tax
    // Composing the final operation
    BiFunction<Double, Double, Double> finalCost =
        addPrices.andThen(applyDiscount).andThen(applyTax);

    // Prices of two items
    double item1 = 50.0;
    double item2 = 100.0;
    // Calculate final cost
    double cost = finalCost.apply(item1, item2);
    // Verify the final calculated cost
    Assertions.assertEquals(144.45D, cost, 0.01);
  }

  @Test
  void intBinaryOperator() {
    IntBinaryOperator add = Integer::sum;
    Assertions.assertEquals(10, add.applyAsInt(4, 6));

    IntStream input = IntStream.of(2, 3, 4);
    OptionalInt result = input.reduce(add);
    Assertions.assertEquals(OptionalInt.of(9), result);
  }

  @Test
  void longBinaryOperator() {
    // Greatest Common Divisor
    LongBinaryOperator gcd =
        (a, b) -> {
          while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
          }
          return a;
        };
    Assertions.assertEquals(6L, gcd.applyAsLong(54L, 24L));

    LongBinaryOperator add = Long::sum;
    // Time car traveled
    LongStream input = LongStream.of(1715785375164L, 1715785385771L);
    final OptionalLong result = input.reduce(add);
    Assertions.assertEquals(OptionalLong.of(3431570760935L), result);
  }

  @Test
  void doubleBinaryOperator() {
    DoubleBinaryOperator subtractAreas = (a, b) -> a - b;
    // Area of a rectangle
    double rectangleArea = 20.0 * 30.0;
    // Area of a circle
    double circleArea = Math.PI * 7.0 * 7.0;

    // Subtract the two areas
    double difference = subtractAreas.applyAsDouble(rectangleArea, circleArea);
    Assertions.assertEquals(446.06, difference, 0.01);

    DoubleBinaryOperator add = Double::sum;
    DoubleStream input = DoubleStream.of(10.2, 5.6, 15.8, 20.12);
    OptionalDouble result = input.reduce(add);
    Assertions.assertEquals(OptionalDouble.of(51.72), result);
  }
}
