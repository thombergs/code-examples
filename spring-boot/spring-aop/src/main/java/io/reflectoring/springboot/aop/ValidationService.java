package io.reflectoring.springboot.aop;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    public void validateNumber(int argument) {
        System.out.println(argument + " is valid");
    }
}
