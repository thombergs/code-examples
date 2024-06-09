package io.reflectoring.validation.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.reflectoring.validation.Input;
import jakarta.validation.Valid;

@Service
@Validated
class ValidatingService {

    void validateInput(@Valid Input input){
      // do something
    }

}
