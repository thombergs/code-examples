package io.reflectoring.functional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ThrowingConsumerTest {
  @ParameterizedTest
  @CsvSource({"50,true", "130,false", "-30,false"})
  void testMethodThatThrowsCheckedException(int percent, boolean valid) {
    // acceptable percentage range: 0 - 100
    ValueRange validPercentageRange = ValueRange.of(0, 100);
    final Function<Integer, String> message =
        input ->
            MessageFormat.format(
                "Percentage {0} should be in range {1}", input, validPercentageRange.toString());

    ThrowingConsumer<Integer> consumer =
        input -> {
          if (!validPercentageRange.isValidValue(input)) {
            throw new ValidationException(message.apply(input));
          }
        };

    if (valid) {
      assertDoesNotThrow(() -> consumer.accept(percent));
    } else {
      assertAll(
          () -> {
            ValidationException exception =
                assertThrows(ValidationException.class, () -> consumer.accept(percent));
            assertEquals(exception.getMessage(), message.apply(percent));
          });
    }
  }

  @TestFactory
  Stream<DynamicTest> testDynamicTestsWithThrowingConsumer() {
    // acceptable percentage range: 0 - 100
    ValueRange validPercentageRange = ValueRange.of(0, 100);
    final Function<Integer, String> message =
        input ->
            MessageFormat.format(
                "Percentage {0} should be in range {1}", input, validPercentageRange.toString());

    // Define the ThrowingConsumer that validates the input percentage
    ThrowingConsumer<TestCase> consumer =
        testCase -> {
          if (!validPercentageRange.isValidValue(testCase.percent)) {
            throw new ValidationException(message.apply(testCase.percent));
          }
        };

    ThrowingConsumer<TestCase> executable =
        testCase -> {
          if (testCase.valid) {
            assertDoesNotThrow(() -> consumer.accept(testCase));
          } else {
            assertAll(
                () -> {
                  ValidationException exception =
                      assertThrows(ValidationException.class, () -> consumer.accept(testCase));
                  assertEquals(exception.getMessage(), message.apply(testCase.percent));
                });
          }
        };
    // Test data: an array of test cases with inputs and their validity
    Collection<TestCase> testCases =
        Arrays.asList(new TestCase(50, true), new TestCase(130, false), new TestCase(-30, false));

    Function<TestCase, String> displayNameGenerator =
        testCase -> "Testing percentage: " + testCase.percent;

    // Generate dynamic tests
    return DynamicTest.stream(testCases.stream(), displayNameGenerator, executable);
  }

  // Helper record to represent a test case
  record TestCase(int percent, boolean valid) {}
}
