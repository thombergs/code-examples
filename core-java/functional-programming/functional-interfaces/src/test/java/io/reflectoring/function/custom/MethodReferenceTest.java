package io.reflectoring.function.custom;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Method reference test. */
public class MethodReferenceTest {
  /** Static method reference. */
  @Test
  void staticMethodReference() {
    List<Integer> numbers = List.of(1, -2, 3, -4, 5);
    List<Integer> positiveNumbers = numbers.stream().map(Math::abs).toList();
    positiveNumbers.forEach(number -> Assertions.assertTrue(number > 0));
  }

  /** The String number comparator. */
  static class StringNumberComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
      if (o1 == null) {
        return o2 == null ? 0 : 1;
      } else if (o2 == null) {
        return -1;
      }
      return o1.compareTo(o2);
    }
  }

  /** Instance method reference. */
  @Test
  void containingClassInstanceMethodReference() {
    List<String> numbers = List.of("One", "Two", "Three");
    List<Integer> numberChars = numbers.stream().map(String::length).toList();
    numberChars.forEach(length -> Assertions.assertTrue(length > 0));
  }

  /** Instance method reference. */
  @Test
  void containingObjectInstanceMethodReference() {
    List<String> numbers = List.of("One", "Two", "Three");
    StringNumberComparator comparator = new StringNumberComparator();
    final List<String> sorted = numbers.stream().sorted(comparator::compare).toList();
    final List<String> expected = List.of("One", "Three", "Two");
    Assertions.assertEquals(expected, sorted);
  }

  /** Instance method arbitrary object particular type. */
  @Test
  void instanceMethodArbitraryObjectParticularType() {
    List<Number> numbers = List.of(1, 2L, 3.0f, 4.0d);
    List<Integer> numberIntValues = numbers.stream().map(Number::intValue).toList();
    Assertions.assertEquals(List.of(1, 2, 3, 4), numberIntValues);
  }

  /** Constructor reference. */
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
    Assertions.assertEquals(expected, numberMapping);
  }
}
