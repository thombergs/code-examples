package io.reflectoring.function.custom;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** The Method reference test. */
public class MethodReferenceTest {
  @Test
  void staticMethodReference() {
    List<Integer> numbers = List.of(1, -2, 3, -4, 5);
    List<Integer> positiveNumbers = numbers.stream().map(Math::abs).toList();
    positiveNumbers.forEach(
        number -> Assertions.assertTrue(number > 0, "Number should be positive."));
  }

  @Test
  void instanceMethodReference() {
    List<String> numbers = List.of("One", "Two", "Three");
    List<Integer> numberChars = numbers.stream().map(String::length).toList();
    numberChars.forEach(length -> Assertions.assertTrue(length > 0, "Number text is not empty."));
  }

  @Test
  void instanceMethodArbitraryObjectParticularType() {
    List<Number> numbers = List.of(1, 2L, 3.0f, 4.0d);
    List<Integer> numberIntValues = numbers.stream().map(Number::intValue).toList();
    Assertions.assertEquals(List.of(1, 2, 3, 4), numberIntValues, "Int values are not same.");
  }

  @Test
  void constructorReference() {
    List<String> numbers = List.of("1", "2", "3");
    Map<String, BigInteger> numberMapping =
        numbers.stream()
            .map(BigInteger::new)
            .collect(Collectors.toMap(BigInteger::toString, Function.identity()));
    Map<String, BigInteger> expected =
        new HashMap<>() {
          {
            put("1", BigInteger.valueOf(1));
            put("2", BigInteger.valueOf(2));
            put("3", BigInteger.valueOf(3));
          }
        };
    Assertions.assertEquals(expected, numberMapping, "Mapped numbers do not match.");
  }
}
