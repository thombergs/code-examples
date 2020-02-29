package io.reflectoring.argumentresolver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ErrorHandler {

  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<?> handleHttpStatusCodeException(NotFoundException e) {
    return ResponseEntity.status(e.getStatusCode()).build();
  }

}
