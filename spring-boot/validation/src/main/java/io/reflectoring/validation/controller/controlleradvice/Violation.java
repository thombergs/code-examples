package io.reflectoring.validation.controller.controlleradvice;

public class Violation {

  private final String fieldName;

  private final String message;

  public Violation(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getMessage() {
    return message;
  }
}
