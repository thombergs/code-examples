package io.reflectoring.resilience4j.ratelimit.services;

import io.reflectoring.resilience4j.ratelimit.model.Flight;
import io.reflectoring.resilience4j.ratelimit.model.SearchRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FlightSearchService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public List<Flight> searchFlights(SearchRequest request) {
        System.out.println("Searching for flights; "
            + "current time = " + LocalDateTime.now().format(formatter) +
            "; current thread = " + Thread.currentThread().getName());

        List<Flight> flights = Arrays.asList(
                new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful");
        return flights;
    }

    public List<Flight> searchFlightsThrowingException(SearchRequest request) throws Exception {
        System.out.println("Searching for flights; "
            + "current time = " + LocalDateTime.now().format(formatter) +
            "; current thread = " + Thread.currentThread().getName());

        throw new Exception("Exception when searching for flights");
    }

}