package io.reflectoring.booking;

import io.reflectoring.booking.business.BookingService;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.customer.CustomerRepository;
import io.reflectoring.flight.FlightRepository;

@EnableJpaRepositories("re")
public class BookingConfiguration {

  @Bean
  public BookingService bookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, FlightRepository flightRepository) {
    return new BookingService(bookingRepository, customerRepository, flightRepository);
  }

}
