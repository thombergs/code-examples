package io.reflectoring.validation;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProgrammerStereotypeValidator implements ConstraintValidator < ProgrammerStereotype, ProgrammerRegisterationRequest > {

  private static final List <String> UNWORTHY_JAVA_IDES = List.of("Notepad", "Netbeans");

  @Override
  public boolean isValid(ProgrammerRegisterationRequest request, ConstraintValidatorContext context) {
    if (request.getProgrammingLanguage().equalsIgnoreCase("Java")) {
      if (UNWORTHY_JAVA_IDES.contains(request.getFavoriteIDE())) {
        return false;
      }
    }
    return true;
  }

}