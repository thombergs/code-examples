package io.reflectoring.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ProgrammerSteriotypeValidationTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenJavaProgrammerUsesNotepad_thenValidationFails() {
    ProgrammerRegisterationRequest request = new ProgrammerRegisterationRequest();
    request.setProgrammingLanguage("Java");
    request.setFavoriteIDE("Notepad");

    var violations = validator.validate(request);

    assertFalse(violations.isEmpty());
    assertThat(violations)
      .extracting(ConstraintViolation::getMessage)
      .contains("Stereotype violation detected! IDE and language not vibing.");
  }

  @Test
  void whenJavaProgrammerUsesIntelliJ_thenValidationSucceeds() {
    ProgrammerRegisterationRequest request = new ProgrammerRegisterationRequest();
    request.setProgrammingLanguage("Java");
    request.setFavoriteIDE("IntelliJ");

    var violations = validator.validate(request);

    assertTrue(violations.isEmpty());
  }

  @Test
  void whenNonJavaProgrammer_thenValidationSucceeds() {
    ProgrammerRegisterationRequest request = new ProgrammerRegisterationRequest();
    request.setProgrammingLanguage("Javascript");
    request.setFavoriteIDE("VS Code");

    var violations = validator.validate(request);

    assertTrue(violations.isEmpty());
  }

}