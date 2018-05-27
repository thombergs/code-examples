package io.reflectoring.booking.business;

import java.util.Optional;

import io.reflectoring.booking.data.Booking;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.customer.data.Customer;
import io.reflectoring.customer.data.CustomerRepository;
import io.reflectoring.flight.data.Flight;
import io.reflectoring.flight.data.FlightService;

public class BookingService {

  private BookingRepository bookingRepository;

  private CustomerRepository customerRepository;

  FlightService flightService;

  public BookingService(
          BookingRepository bookingRepository,
          CustomerRepository customerRepository,
          FlightService flightService) {
    this.bookingRepository = bookingRepository;
    this.customerRepository = customerRepository;
    this.flightService = flightService;
  }

  /**
   * Books the given flight for the given customer.
   */
  public Booking bookFlight(Long customerId, String flightNumber) {

    Optional<Customer> customer = customerRepository.findById(customerId);
    if (!customer.isPresent()) {
      throw new CustomerDoesNotExistException(customerId);
    }

    Optional<Flight> flight = flightService.findFlight(flightNumber);
    if (!flight.isPresent()) {
      throw new FlightDoesNotExistException(flightNumber);
    }

    Booking booking = Booking.builder()
            .customer(customer.get())
            .flightNumber(flight.get().getFlightNumber())
            .build();

    return this.bookingRepository.save(booking);
  }

}
