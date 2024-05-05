package io.reflectoring.function.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ArithmeticOperationTest {

  @Test
  void operate() {
    // Define operations
    ArithmeticOperation add = (a, b) -> a + b;
    ArithmeticOperation subtract = (a, b) -> a - b;
    ArithmeticOperation multiply = (a, b) -> a * b;
    ArithmeticOperation divide = (a, b) -> a / b;

    // Use the operations
    int addition = add.operate(10, 5); // Returns 15
    int subtraction = subtract.operate(10, 5); // Returns 5
    int multiplication = multiply.operate(10, 5); // Returns 50
    int division = divide.operate(10, 5); // Returns 2

    // Verify results
    assertEquals(15, addition, "Result of addition is not correct.");
    assertEquals(5, subtraction, "Result of subtraction is not correct.");
    assertEquals(50, multiplication, "Result of multiplication is not correct.");
    assertEquals(2, division, "Result of division is not correct.");
  }
}
