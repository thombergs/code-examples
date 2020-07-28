package io.reflectoring.resilience4j.retry.services;

import io.reflectoring.resilience4j.retry.model.Flight;
import io.reflectoring.resilience4j.retry.model.SearchRequest;
import io.reflectoring.resilience4j.retry.model.SearchResponse;
import io.reflectoring.resilience4j.retry.services.failures.NoFailure;
import io.reflectoring.resilience4j.retry.services.failures.PotentialFailure;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlightSearchService {
    PotentialFailure potentialFailure = new NoFailure();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public List<Flight> searchFlights(SearchRequest request) {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
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

    public List<Flight> searchFlightsThrowingException(SearchRequest request) throws Exception {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
        throw new Exception("Exception when searching for flights");
    }

    public void setPotentialFailure(PotentialFailure potentialFailure) {
        this.potentialFailure = potentialFailure;
    }

    public SearchResponse httpSearchFlights(SearchRequest request) throws IOException {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
        potentialFailure.occur();

        String date = request.getFlightDate();
        String from = request.getFrom();
        String to = request.getTo();
        if (request.getFlightDate().equals("07/25/2020")) { // Simulating an error scenario
            System.out.println("Flight data initialization in progress, cannot search at this time");
            SearchResponse response = new SearchResponse();
            response.setErrorCode("FS-167");
            response.setFlights(Collections.emptyList());
            return response;
        }

        List<Flight> flights = Arrays.asList(
                new Flight("XY 765", date, from, to),
                new Flight("XY 781", date, from, to),
                new Flight("XY 732", date, from, to),
                new Flight("XY 746", date, from, to)
        );
        System.out.println("Flight search successful");
        SearchResponse response = new SearchResponse();
        response.setFlights(flights);
        return response;
    }
}