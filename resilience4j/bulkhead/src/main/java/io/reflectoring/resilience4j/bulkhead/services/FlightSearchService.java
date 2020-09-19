package io.reflectoring.resilience4j.bulkhead.services;

import io.reflectoring.resilience4j.bulkhead.model.Flight;
import io.reflectoring.resilience4j.bulkhead.model.SearchRequest;
import io.reflectoring.resilience4j.bulkhead.utils.RequestTrackingIdHolder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FlightSearchService {
    Random random = new Random();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public List<Flight> searchFlightsTakingRandomTime(SearchRequest request) {
        long delay = random.nextInt(3000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public List<Flight> searchFlightsTakingOneSecond(SearchRequest request) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Searching for flights; "
            + "current time = " + LocalDateTime.now().format(formatter) +
            "; current thread = " + Thread.currentThread().getName());

        List<Flight> flights = Arrays.asList(
            new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful at " + LocalDateTime.now().format(formatter));
        return flights;
    }

    public List<Flight> searchFlightsTakingOneSecond_PrintCorrelationId(SearchRequest request) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Searching for flights; "
            + "current time = " + LocalDateTime.now().format(formatter) +
            "; current thread = " + Thread.currentThread().getName() + "; Request Tracking Id = " + RequestTrackingIdHolder.getRequestTrackingId());

        List<Flight> flights = Arrays.asList(
            new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful at " + LocalDateTime.now().format(formatter));
        return flights;
    }
}