package io.reflectoring.validation.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.reflectoring.validation.Input;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class ValidatingServiceTest {

  @Autowired
  private ValidatingService service;

  @Test
  void whenInputIsValid_thenThrowsNoException(){
    Input input = new Input();
    input.setNumberBetweenOneAndTen(5);
    input.setIpAddress("111.111.111.111");

    service.validateInput(input);

    // then no exception
  }

  @Test
  void whenInputIsInvalid_thenThrowsException(){
    Input input = new Input();
    input.setNumberBetweenOneAndTen(0);
    input.setIpAddress("invalid");

    assertThrows(ConstraintViolationException.class, () -> {
      service.validateInput(input);
    });
  }

}