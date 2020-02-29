package io.reflectoring.argumentresolver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
class ErrorHandler {

  @ExceptionHandler(HttpStatusCodeException.class)
  ResponseEntity<?> handleHttpStatusCodeException(HttpStatusCodeException e) {
    return ResponseEntity.status(e.getStatusCode()).build();
  }

}
