package io.reflectoring.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.reflectoring.validation.service.ProgrammaticallyValidatingService;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProgrammaticallyValidatingServiceTest {

  @Autowired
  private ProgrammaticallyValidatingService service;

  @Test
  void whenInputIsInvalid_thenThrowsException(){
    Input input = new Input();
    input.setNumberBetweenOneAndTen(0);
    input.setIpAddress("invalid");

    assertThrows(ConstraintViolationException.class, () -> {
      service.validateInput(input);
    });
  }

  @Test
  void givenInjectedValidator_whenInputIsInvalid_thenThrowsException(){
    Input input = new Input();
    input.setNumberBetweenOneAndTen(0);
    input.setIpAddress("invalid");

    assertThrows(ConstraintViolationException.class, () -> {
      service.validateInputWithInjectedValidator(input);
    });
  }

}