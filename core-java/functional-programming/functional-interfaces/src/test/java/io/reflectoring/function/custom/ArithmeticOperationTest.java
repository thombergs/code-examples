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

    // Verify results
    assertEquals(15, add.operate(10, 5));
    assertEquals(5, subtract.operate(10, 5));
    assertEquals(50, multiply.operate(10, 5));
    assertEquals(2, divide.operate(10, 5));
  }
}
