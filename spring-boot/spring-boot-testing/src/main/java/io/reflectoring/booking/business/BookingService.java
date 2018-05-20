package io.reflectoring.booking.business;

import java.util.Optional;

import io.reflectoring.booking.data.Booking;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.customer.Customer;
import io.reflectoring.customer.CustomerRepository;
import io.reflectoring.flight.Flight;
import io.reflectoring.flight.FlightRepository;

public class BookingService {

  private BookingRepository bookingRepository;

  private CustomerRepository customerRepository;

  private FlightRepository flightRepository;

  public BookingService(
          BookingRepository bookingRepository,
          CustomerRepository customerRepository,
          FlightRepository flightRepository) {
    this.bookingRepository = bookingRepository;
    this.customerRepository = customerRepository;
    this.flightRepository = flightRepository;
  }

  /**
   * Books the given flight for the given customer.
   */
  public Booking bookFlight(Long customerId, Long flightId) {

    Optional<Customer> customer = customerRepository.findById(customerId);
    if (!customer.isPresent()) {
      throw new CustomerDoesNotExistException(customerId);
    }

    Optional<Flight> flight = flightRepository.findById(flightId);
    if (!flight.isPresent()) {
      throw new FlightDoesNotExistException(flightId);
    }

    Booking booking = Booking.builder()
            .customer(customer.get())
            .flight(flight.get())
            .build();

    return this.bookingRepository.save(booking);
  }

}
