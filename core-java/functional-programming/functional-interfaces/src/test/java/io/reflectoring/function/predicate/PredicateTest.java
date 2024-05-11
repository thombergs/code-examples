package io.reflectoring.function.predicate;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
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
}
