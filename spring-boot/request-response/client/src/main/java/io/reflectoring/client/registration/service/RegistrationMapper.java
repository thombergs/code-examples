package io.reflectoring.client.registration.service;

import io.reflectoring.client.dto.RegistrationDto;
import io.reflectoring.client.registration.persistance.Registration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    public Registration toRegistration(RegistrationDto registrationDto){
        return Registration.builder()
                .id(registrationDto.getId())
                .date(registrationDto.getDate())
                .owner(registrationDto.getOwner())
                .signature(registrationDto.getSignature())
                .build();
    }
}
