package io.reflectoring.resilience4j.springboot.services;

import io.reflectoring.resilience4j.springboot.model.Flight;
import io.reflectoring.resilience4j.springboot.model.SearchRequest;
import io.reflectoring.resilience4j.springboot.model.SearchResponse;
import io.reflectoring.resilience4j.springboot.services.failures.NoCheckedExceptionFailure;
import io.reflectoring.resilience4j.springboot.services.failures.NoFailure;
import io.reflectoring.resilience4j.springboot.services.failures.PotentialFailure;
import io.reflectoring.resilience4j.springboot.services.failures.PotentialFailureCheckedException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class FlightSearchService {

  PotentialFailure potentialFailure = new NoFailure();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
  Random random = new Random();

  PotentialFailureCheckedException potentialFailureCheckedException = new NoCheckedExceptionFailure();

  public List<Flight> searchFlights(SearchRequest request) {
    System.out
        .println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
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
    try {
      if (!potentialFailureCheckedException.occur()) {
        List<Flight> flights = Arrays.asList(
            new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Flight search successful");
        return flights;
      }
    } catch (RuntimeException re) {
      throw new Exception("Exception when searching for flights");
    }
    return Collections.EMPTY_LIST;
  }

  public void setPotentialFailure(PotentialFailure potentialFailure) {
    this.potentialFailure = potentialFailure;
  }

  public void setPotentialFailureCheckedException(
      PotentialFailureCheckedException potentialFailureCheckedException) {
    this.potentialFailureCheckedException = potentialFailureCheckedException;
  }

  public SearchResponse httpSearchFlights(SearchRequest request) throws IOException {
    System.out
        .println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));

    String date = request.getFlightDate();
    String from = request.getFrom();
    String to = request.getTo();
    if (request.getFlightDate().equals("01/25/2021")) { // Simulating an error scenario
      try {
        if (!potentialFailure.occur()) {
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
      } catch (RuntimeException re) {
        System.out.println("Flight data initialization in progress, cannot search at this time");
        SearchResponse response = new SearchResponse();
        response.setErrorCode("FS-167");
        response.setFlights(Collections.emptyList());
        return response;
      }
    }

    potentialFailure.occur();
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

  public List<Flight> searchFlightsTakingOneSecond(SearchRequest request) {
    System.out.println("Searching for flights; "
        + "current time = " + LocalDateTime.now().format(formatter) +
        "; current thread = " + Thread.currentThread().getName());

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<Flight> flights = Arrays.asList(
        new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
        new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
    );
    System.out.println("Flight search successful at " + LocalDateTime.now().format(formatter));
    return flights;
  }

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
}