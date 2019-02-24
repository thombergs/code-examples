package io.reflectoring.booking;

import io.reflectoring.customer.data.Customer;
import io.reflectoring.customer.data.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookingIntegrationTest {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private CustomerRepository customerRepository;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .build();
  }

  @Test
  void bookFlightReturnsHttpStatusOk() throws Exception {
    this.customerRepository.save(Customer.builder()
            .name("Hurley")
            .build());

    this.mockMvc.perform(
            post("/booking")
                    .param("customerId", "1")
                    .param("flightNumber", "Oceanic 815"))
            .andExpect(status().isOk());
  }

}
