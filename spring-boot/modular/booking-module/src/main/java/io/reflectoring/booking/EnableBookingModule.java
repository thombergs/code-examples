package io.reflectoring.booking;

import java.lang.annotation.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(BookingModuleConfiguration.class)
@Configuration
public @interface EnableBookingModule {
}
