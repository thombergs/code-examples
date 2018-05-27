package io.reflectoring.booking;

import io.reflectoring.booking.business.BookingService;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.customer.CustomerConfiguration;
import io.reflectoring.customer.data.CustomerRepository;
import io.reflectoring.flight.FlightConfiguration;
import io.reflectoring.flight.data.FlightService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootConfiguration
@Import({CustomerConfiguration.class, FlightConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
public class BookingConfiguration {

  @Bean
  public BookingService bookingService(
          BookingRepository bookingRepository,
          CustomerRepository customerRepository,
          FlightService flightService) {
    return new BookingService(bookingRepository, customerRepository, flightService);
  }

}
