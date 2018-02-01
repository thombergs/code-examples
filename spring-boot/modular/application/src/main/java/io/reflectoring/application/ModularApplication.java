package io.reflectoring.application;

import io.reflectoring.booking.BookingModuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BookingModuleConfiguration.class)
public class ModularApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModularApplication.class, args);
	}

}
