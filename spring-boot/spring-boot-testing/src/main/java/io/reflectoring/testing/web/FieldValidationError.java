package io.reflectoring.testing.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldValidationError {

  private String field;

  private String message;

  public FieldValidationError(@JsonProperty("field") String field, @JsonProperty("message") String message) {
    this.field = field;
    this.message = message;
  }
}