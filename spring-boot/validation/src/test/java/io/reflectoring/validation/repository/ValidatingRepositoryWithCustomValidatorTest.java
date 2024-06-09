package io.reflectoring.validation.repository;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;

import io.reflectoring.validation.InputWithCustomValidator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ValidatingRepositoryWithCustomValidatorTest {

  @Autowired
  private ValidatingRepositoryWithCustomValidator repository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void whenInputIsInvalid_thenThrowsException() {
    InputWithCustomValidator input = invalidInput();
    try {
      repository.save(input);
      entityManager.flush();
      Assertions.fail("expected ConstraintViolationException");
    } catch (ConstraintViolationException e) {
      assertThat(e.getConstraintViolations()).hasSize(2);
    }
  }

  private InputWithCustomValidator invalidInput() {
    InputWithCustomValidator input = new InputWithCustomValidator();
    input.setNumberBetweenOneAndTen(0);
    input.setIpAddress("invalid");
    return input;
  }

}
