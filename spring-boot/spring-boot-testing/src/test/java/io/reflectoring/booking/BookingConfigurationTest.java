package io.reflectoring.booking;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = BookingConfiguration.class)
class BookingConfigurationTest {

  @Autowired
  private ApplicationContext applicationContext;


  @BeforeEach
  void printApplicationContext() {
    Arrays.stream(applicationContext.getBeanDefinitionNames())
            .map(name -> applicationContext.getBean(name).getClass().getName())
            .sorted()
            .forEach(System.out::println);
  }
  @Test
  void bookingConfigurationLoads() {
  }

}