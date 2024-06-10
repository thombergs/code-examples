package io.reflectoring.validation.controller.controlleradvice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
class ErrorHandlingControllerAdvice {
	
  private static final String VIOLATIONS_KEY = "violations";

  @ExceptionHandler(ConstraintViolationException.class)
  ProblemDetail handle(ConstraintViolationException exception) {
    List < Violation > violations = new ArrayList < > ();
    for (ConstraintViolation violation: exception.getConstraintViolations()) {
      violations.add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
    }

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failure.");
    problemDetail.setProperty(VIOLATIONS_KEY, violations);
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ProblemDetail handle(MethodArgumentNotValidException exception) {
    List < Violation > violations = new ArrayList < > ();
    for (FieldError fieldError: exception.getBindingResult().getFieldErrors()) {
      violations.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failure.");
    problemDetail.setProperty(VIOLATIONS_KEY, violations);
    return problemDetail;
  }

}