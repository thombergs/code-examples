package io.reflectoring.function;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PredicateTest {
  // C = Carpenter, W = Welder
  private final Object[][] workers = {
    {"C", 24},
    {"W", 32},
    {"C", 35},
    {"W", 40},
    {"C", 50},
    {"W", 44},
    {"C", 30}
  };

  @Test
  void testFiltering() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Predicate<Integer> isEven = num -> num % 2 == 0;

    List<Integer> actual = numbers.stream().filter(isEven).toList();

    List<Integer> expected = List.of(2, 4, 6, 8, 10);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testPredicate() {
    List<Integer> numbers = List.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5);
    Predicate<Integer> isZero = num -> num == 0;
    Predicate<Integer> isPositive = num -> num > 0;
    Predicate<Integer> isNegative = num -> num < 0;
    Predicate<Integer> isOdd = num -> num % 2 == 1;

    Predicate<Integer> isPositiveOrZero = isPositive.or(isZero);
    Predicate<Integer> isPositiveAndOdd = isPositive.and(isOdd);
    Predicate<Integer> isNotPositive = Predicate.not(isPositive);
    Predicate<Integer> isNotZero = isZero.negate();
    Predicate<Integer> isAlsoZero = isPositive.negate().and(isNegative.negate());

    // check zero or greater
    Assertions.assertEquals(
        List.of(0, 1, 2, 3, 4, 5), numbers.stream().filter(isPositiveOrZero).toList());

    // check greater than zero and odd
    Assertions.assertEquals(List.of(1, 3, 5), numbers.stream().filter(isPositiveAndOdd).toList());

    // check less than zero and negative
    Assertions.assertEquals(
        List.of(-5, -4, -3, -2, -1, 0), numbers.stream().filter(isNotPositive).toList());

    // check not zero
    Assertions.assertEquals(
        List.of(-5, -4, -3, -2, -1, 1, 2, 3, 4, 5), numbers.stream().filter(isNotZero).toList());

    // check neither positive nor negative
    Assertions.assertEquals(
        numbers.stream().filter(isZero).toList(), numbers.stream().filter(isAlsoZero).toList());
  }

  @Test
  void testBiPredicate() {

    BiPredicate<String, Integer> juniorCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 18 && age <= 40);

    BiPredicate<String, Integer> groomedCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 30 && age <= 40);

    BiPredicate<String, Integer> allCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 18);

    BiPredicate<String, Integer> juniorWelderCheck =
        (worker, age) -> "W".equals(worker) && (age >= 18 && age <= 40);

    BiPredicate<String, Integer> juniorWorkerCheck = juniorCarpenterCheck.or(juniorWelderCheck);

    BiPredicate<String, Integer> juniorGroomedCarpenterCheck =
        juniorCarpenterCheck.and(groomedCarpenterCheck);

    BiPredicate<String, Integer> allWelderCheck = allCarpenterCheck.negate();

    final long juniorCarpenterCount =
        Arrays.stream(workers)
            .filter(person -> juniorCarpenterCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(3L, juniorCarpenterCount);

    final long juniorWelderCount =
        Arrays.stream(workers)
            .filter(person -> juniorWelderCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(2L, juniorWelderCount);

    final long juniorWorkerCount =
        Arrays.stream(workers)
            .filter(person -> juniorWorkerCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(5L, juniorWorkerCount);

    final long juniorGroomedCarpenterCount =
        Arrays.stream(workers)
            .filter(
                person -> juniorGroomedCarpenterCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(2L, juniorGroomedCarpenterCount);

    final long allWelderCount =
        Arrays.stream(workers)
            .filter(person -> allWelderCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(3L, allWelderCount);
  }

  @Test
  void testBiPredicateDefaultMethods() {

    BiPredicate<String, Integer> juniorCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 18 && age <= 40);

    BiPredicate<String, Integer> groomedCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 30 && age <= 40);

    BiPredicate<String, Integer> allCarpenterCheck =
        (worker, age) -> "C".equals(worker) && (age >= 18);

    BiPredicate<String, Integer> juniorWelderCheck =
        (worker, age) -> "W".equals(worker) && (age >= 18 && age <= 40);

    BiPredicate<String, Integer> juniorWorkerCheck = juniorCarpenterCheck.or(juniorWelderCheck);

    BiPredicate<String, Integer> juniorGroomedCarpenterCheck =
        juniorCarpenterCheck.and(groomedCarpenterCheck);

    BiPredicate<String, Integer> allWelderCheck = allCarpenterCheck.negate();

    // test or()
    final long juniorWorkerCount =
        Arrays.stream(workers)
            .filter(person -> juniorWorkerCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(5L, juniorWorkerCount);

    // test and()
    final long juniorGroomedCarpenterCount =
        Arrays.stream(workers)
            .filter(
                person -> juniorGroomedCarpenterCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(2L, juniorGroomedCarpenterCount);

    // test negate()
    final long allWelderCount =
        Arrays.stream(workers)
            .filter(person -> allWelderCheck.test((String) person[0], (Integer) person[1]))
            .count();
    Assertions.assertEquals(3L, allWelderCount);
  }

  @Test
  void testIntPredicate() {
    IntPredicate isZero = num -> num == 0;
    IntPredicate isPositive = num -> num > 0;
    IntPredicate isNegative = num -> num < 0;
    IntPredicate isOdd = num -> num % 2 == 1;

    IntPredicate isPositiveOrZero = isPositive.or(isZero);
    IntPredicate isPositiveAndOdd = isPositive.and(isOdd);
    IntPredicate isNotZero = isZero.negate();
    IntPredicate isAlsoZero = isPositive.negate().and(isNegative.negate());

    // check zero or greater
    Assertions.assertArrayEquals(
        new int[] {0, 1, 2, 3, 4, 5}, IntStream.range(-5, 6).filter(isPositiveOrZero).toArray());

    // check greater than zero and odd
    Assertions.assertArrayEquals(
        new int[] {1, 3, 5}, IntStream.range(-5, 6).filter(isPositiveAndOdd).toArray());

    // check not zero
    Assertions.assertArrayEquals(
        new int[] {-5, -4, -3, -2, -1, 1, 2, 3, 4, 5},
        IntStream.range(-5, 6).filter(isNotZero).toArray());

    // check neither positive nor negative
    Assertions.assertArrayEquals(
        IntStream.range(-5, 6).filter(isZero).toArray(),
        IntStream.range(-5, 6).filter(isAlsoZero).toArray());
  }

  @Test
  void testLongPredicate() {
    LongPredicate isStopped = num -> num == 0;
    LongPredicate firstGear = num -> num > 0 && num <= 20;
    LongPredicate secondGear = num -> num > 20 && num <= 35;
    LongPredicate thirdGear = num -> num > 35 && num <= 50;
    LongPredicate forthGear = num -> num > 50 && num <= 80;
    LongPredicate fifthGear = num -> num > 80;
    LongPredicate max = num -> num < 150;

    LongPredicate cityDriveCheck = firstGear.or(secondGear).or(thirdGear);
    LongPredicate drivingCheck = isStopped.negate();
    LongPredicate highwayCheck = max.and(forthGear.or(fifthGear));

    // check stopped
    Assertions.assertArrayEquals(
        new long[] {0L}, LongStream.of(0L, 40L, 60L, 100L).filter(isStopped).toArray());

    // check city speed limit
    Assertions.assertArrayEquals(
        new long[] {20L, 50L}, LongStream.of(0L, 20L, 50L, 100L).filter(cityDriveCheck).toArray());

    // check negate
    Assertions.assertArrayEquals(
        new long[] {70L, 100L}, LongStream.of(70L, 100L, 200L).filter(highwayCheck).toArray());
  }

  @Test
  void testDoublePredicate() {
    // weight categories (weight in lbs)
    DoublePredicate underweight = weight -> weight <= 125;
    DoublePredicate healthy = weight -> weight >= 126 && weight <= 168;
    DoublePredicate overweight = weight -> weight >= 169 && weight <= 202;
    DoublePredicate obese = weight -> weight >= 203;
    DoublePredicate needToLose = weight -> weight >= 169;
    DoublePredicate notHealthy = healthy.negate();
    DoublePredicate alsoNotHealthy = underweight.or(overweight).or(obese);
    DoublePredicate skipSugar = needToLose.and(overweight.or(obese));

    // check need to lose weight
    Assertions.assertArrayEquals(
        new double[] {200D}, DoubleStream.of(100D, 140D, 160D, 200D).filter(needToLose).toArray());

    // check need to lose weight
    Assertions.assertArrayEquals(
        new double[] {100D, 200D},
        DoubleStream.of(100D, 140D, 160D, 200D).filter(notHealthy).toArray());

    // check negate()
    Assertions.assertArrayEquals(
        DoubleStream.of(100D, 140D, 160D, 200D).filter(notHealthy).toArray(),
        DoubleStream.of(100D, 140D, 160D, 200D).filter(alsoNotHealthy).toArray());

    // check and()
    Assertions.assertArrayEquals(
        new double[] {200D}, DoubleStream.of(100D, 140D, 160D, 200D).filter(skipSugar).toArray());
  }
}
