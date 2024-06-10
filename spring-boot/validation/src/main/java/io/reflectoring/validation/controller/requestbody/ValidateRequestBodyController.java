package io.reflectoring.validation.controller.requestbody;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.reflectoring.validation.Input;
import jakarta.validation.Valid;

@RestController
class ValidateRequestBodyController {

  @PostMapping("/validate-request-body")
  ResponseEntity<Void> validateBody(@Valid @RequestBody Input input) {
    return ResponseEntity.ok().build();
  }

}
