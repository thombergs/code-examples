package io.reflectoring.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = IpAddressValidator.class)
public @interface IpAddress {

  String message() default "{ip-address.invalid}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}