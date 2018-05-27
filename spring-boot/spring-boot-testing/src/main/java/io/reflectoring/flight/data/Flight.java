package io.reflectoring.flight.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Flight {

  private String flightNumber;

  private String airline;

}
