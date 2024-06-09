package io.reflectoring.validation.controller.controlleradvice;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse {

  private List<Violation> violations = new ArrayList<>();

}
