package io.reflectoring.customer.web;

import io.reflectoring.booking.web.BookingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest
class CustomerModuleWebLayerTests {

  @Autowired
  private CustomerController customerController;

  @Autowired(required = false)
  private BookingController bookingController;

  @Test
  void onlyCustomerControllerIsLoaded() {
    assertThat(customerController).isNotNull();
    assertThat(bookingController).isNull();
  }

}