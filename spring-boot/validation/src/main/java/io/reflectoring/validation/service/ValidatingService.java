package io.reflectoring.validation.service;

import javax.validation.Valid;

import io.reflectoring.validation.Input;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
class ValidatingService {

    void validateInput(@Valid Input input){
      // do something
    }

}
