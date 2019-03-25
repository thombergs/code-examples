package io.reflectoring.customer.data;

import io.reflectoring.booking.data.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CustomerModuleDataLayerTests {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired(required = false)
  private BookingRepository bookingRepository;

  @Test
  void onlyCustomerRepositoryIsLoaded() {
    assertThat(customerRepository).isNotNull();
    assertThat(bookingRepository).isNull();
  }

  @Test
  void versionTest(){
    Customer hans = new Customer();
    hans.setName("Hans");
    customerRepository.save(hans);
  }

}