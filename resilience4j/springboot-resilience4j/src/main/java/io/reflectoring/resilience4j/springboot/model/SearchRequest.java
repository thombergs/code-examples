package io.reflectoring.resilience4j.springboot.model;

public class SearchRequest {
    String from;
    String to;
    String flightDate;

    public SearchRequest(String from, String to, String flightDate) {
        this.from = from;
        this.to = to;
        this.flightDate = flightDate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFlightDate() {
        return flightDate;
    }
}
