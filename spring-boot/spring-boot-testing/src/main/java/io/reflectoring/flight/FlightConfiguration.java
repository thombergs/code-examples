package io.reflectoring.flight;

import io.reflectoring.flight.data.FlightService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan
public class FlightConfiguration {

  @Bean
  public FlightService flightService(){
    return new FlightService();
  }

}
