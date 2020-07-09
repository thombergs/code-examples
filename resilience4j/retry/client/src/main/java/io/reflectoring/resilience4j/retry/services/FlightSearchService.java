package io.reflectoring.resilience4j.retry.services;

import io.reflectoring.resilience4j.retry.model.Flight;
import io.reflectoring.resilience4j.retry.model.SearchRequest;
import io.reflectoring.resilience4j.retry.model.SearchResponse;
import io.reflectoring.resilience4j.retry.services.failures.NoFailure;
import io.reflectoring.resilience4j.retry.services.failures.PotentialFailure;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class FlightSearchService {
    PotentialFailure potentialFailure = new NoFailure();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
    ObjectMapper mapper = new ObjectMapper();

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
        potentialFailure.occur();

        throw new Exception("Exception when searching for flights");
    }

    public void setPotentialFailure(PotentialFailure potentialFailure) {
        this.potentialFailure = potentialFailure;
    }

    public SearchResponse httpSearchFlights(SearchRequest request) throws IOException {
        System.out.println("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
        potentialFailure.occur();

        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://localhost:8080/flights/search?from=" + request.getFrom() + "&to=" + request.getTo() + "&date=" + request.getFlightDate();
        HttpResponse response = client.execute(new HttpGet(url));
        String body = EntityUtils.toString(response.getEntity());
        return mapper.readValue(body, SearchResponse.class);
    }
}