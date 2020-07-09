package io.reflectoring.resilience4j.retry.flightservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class FlightController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    @GetMapping("/search")
    public SearchResponse searchFlights(@RequestParam(value = "from") String from,
                                        @RequestParam(value = "to") String to,
                                        @RequestParam(value = "date") String date) {
        log.info("Searching for flights; current time = " + LocalDateTime.now().format(formatter));

        if (date.equals("07/25/2020")) { // Simulating an error scenario
            log.info("Flight data initialization in progress, cannot search at this time");
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
        log.info("Flight search successful");
        SearchResponse response = new SearchResponse();
        response.setFlights(flights);
        return response;
    }
}