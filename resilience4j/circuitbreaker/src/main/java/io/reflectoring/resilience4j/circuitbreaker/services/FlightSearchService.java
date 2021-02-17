package io.reflectoring.resilience4j.circuitbreaker.services;

import io.reflectoring.resilience4j.circuitbreaker.model.Flight;
import io.reflectoring.resilience4j.circuitbreaker.model.SearchRequest;
import io.reflectoring.resilience4j.circuitbreaker.services.delays.NoDelay;
import io.reflectoring.resilience4j.circuitbreaker.services.delays.PotentialDelay;
import io.reflectoring.resilience4j.circuitbreaker.services.failures.NoFailure;
import io.reflectoring.resilience4j.circuitbreaker.services.failures.PotentialFailure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FlightSearchService {
    PotentialFailure potentialFailure = new NoFailure();
    PotentialDelay potentialDelay = new NoDelay();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public List<Flight> searchFlights(SearchRequest request) {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
        potentialDelay.occur();
        potentialFailure.occur();

        List<Flight> flights = Arrays.asList(
                new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful");
        return flights;
    }

    public List<Flight> searchFlightsTakingTwoSeconds(SearchRequest request) {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Flight> flights = Arrays.asList(
                new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
                new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful");
        return flights;
    }

    public void setPotentialFailure(PotentialFailure potentialFailure) {
        this.potentialFailure = potentialFailure;
    }

    public void setPotentialDelay(PotentialDelay potentialDelay) {
        this.potentialDelay = potentialDelay;
    }
}