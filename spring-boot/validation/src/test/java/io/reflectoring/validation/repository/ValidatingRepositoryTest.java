package io.reflectoring.validation.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.reflectoring.validation.Input;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ValidatingRepositoryTest {

  @Autowired
  private ValidatingRepository repository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void whenInputIsInvalid_thenThrowsException() {
    Input input = invalidInput();

    assertThrows(ConstraintViolationException.class, () -> {
      repository.save(input);
      entityManager.flush();
    });
  }

  private Input invalidInput() {
    Input input = new Input();
    input.setNumberBetweenOneAndTen(0);
    input.setIpAddress("invalid");
    return input;
  }

}
