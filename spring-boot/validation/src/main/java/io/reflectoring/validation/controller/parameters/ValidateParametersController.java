package io.reflectoring.validation.controller.parameters;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
class ValidateParametersController {

  @GetMapping("/validatePathVariable/{id}")
  ResponseEntity<String> validatePathVariable(@PathVariable("id") @Min(5) int id) {
    return ResponseEntity.ok("valid");
  }

  @GetMapping("/validateRequestParameter")
  ResponseEntity<String> validateRequestParameter(@RequestParam("param") @Min(5) int param) {
    return ResponseEntity.ok("valid");
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  String handleConstraintViolationException(ConstraintViolationException e) {
    return "not valid due to validation error: " + e.getMessage();
  }


}
