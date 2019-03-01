package io.reflectoring.customer;

import io.reflectoring.booking.business.BookingService;
import io.reflectoring.booking.data.BookingRepository;
import io.reflectoring.booking.web.BookingController;
import io.reflectoring.customer.business.CustomerService;
import io.reflectoring.customer.data.CustomerRepository;
import io.reflectoring.customer.web.CustomerController;
import io.reflectoring.flight.data.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerModuleTest {

  @Autowired(required = false)
  private BookingController bookingController;
  @Autowired(required = false)
  private BookingService bookingService;
  @Autowired(required = false)
  private BookingRepository bookingRepository;

  @Autowired
  private CustomerController customerController;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerRepository customerRepository;

  @Test
  void onlyCustomerModuleIsLoaded() {
    assertThat(customerController).isNotNull();
    assertThat(customerService).isNotNull();
    assertThat(customerRepository).isNotNull();
    assertThat(bookingController).isNull();
    assertThat(bookingService).isNull();
    assertThat(bookingRepository).isNull();
  }

}