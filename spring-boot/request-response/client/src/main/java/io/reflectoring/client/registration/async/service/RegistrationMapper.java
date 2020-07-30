package io.reflectoring.client.registration.async.service;

import io.reflectoring.client.dto.RegistrationDto;
import io.reflectoring.client.registration.async.persistance.Registration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    Registration toRegistration(RegistrationDto registrationDto){
        return Registration.builder()
                .id(registrationDto.getId())
                .date(registrationDto.getDate())
                .owner(registrationDto.getOwner())
                .signature(registrationDto.getSignature())
                .build();
    }
}
