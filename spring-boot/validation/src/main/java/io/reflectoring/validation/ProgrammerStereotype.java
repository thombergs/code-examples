package io.reflectoring.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ProgrammerStereotypeValidator.class)
public @interface ProgrammerStereotype {

  String message() default "Stereotype violation detected! IDE and language not vibing.";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
