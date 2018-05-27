package io.reflectoring.flight.data;

import java.util.Optional;

public class FlightService {

  public Optional<Flight> findFlight(String flightNumber) {
    return Optional.of(Flight.builder()
            .airline("Oceanic")
            .flightNumber("815")
            .build());
  }

}
