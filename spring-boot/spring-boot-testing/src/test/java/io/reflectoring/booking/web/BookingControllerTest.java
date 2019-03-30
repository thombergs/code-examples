package io.reflectoring.booking.web;

import java.util.Arrays;

import io.reflectoring.booking.business.BookingService;
import io.reflectoring.booking.data.Booking;
import io.reflectoring.customer.data.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ApplicationContext applicationContext;

  @MockBean
  private BookingService bookingService;

  @BeforeEach
  void printApplicationContext() {
    Arrays.stream(applicationContext.getBeanDefinitionNames())
            .map(name -> applicationContext.getBean(name).getClass().getName())
            .sorted()
            .forEach(System.out::println);
  }

  @Test
  void bookFlightReturnsHttpStatusOk() throws Exception {
    when(bookingService.bookFlight(eq(42L), eq("Oceanic 815")))
            .thenReturn(expectedBooking());

    mockMvc.perform(
            post("/booking")
                    .param("customerId", "42")
                    .param("flightNumber", "Oceanic 815"))
            .andExpect(status().isOk());
  }

  private Booking expectedBooking() {
    return Booking.builder()
            .customer(Customer.builder()
                    .id(42L)
                    .name("Zaphod")
                    .build())
            .flightNumber("Oceanic 815")
            .build();
  }

}