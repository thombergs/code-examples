package io.reflectoring.booking;

import io.reflectoring.booking.business.BookingService;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.customer.CustomerConfiguration;
import io.reflectoring.customer.data.CustomerRepository;
import io.reflectoring.flight.FlightConfiguration;
import io.reflectoring.flight.data.FlightService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CustomerConfiguration.class, FlightConfiguration.class})
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
