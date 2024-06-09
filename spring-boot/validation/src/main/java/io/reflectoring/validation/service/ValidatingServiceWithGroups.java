package io.reflectoring.validation.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.reflectoring.validation.InputWithCustomValidator;
import jakarta.validation.Valid;

@Service
@Validated
class ValidatingServiceWithGroups {

    @Validated(OnCreate.class)
    void validateForCreate(@Valid InputWithCustomValidator input){
      // do something
    }

    @Validated(OnUpdate.class)
    void validateForUpdate(@Valid InputWithCustomValidator input){
        // do something
    }

}
