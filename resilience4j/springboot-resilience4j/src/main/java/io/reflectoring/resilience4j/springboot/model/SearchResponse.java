package io.reflectoring.resilience4j.springboot.model;

import java.util.List;

public class SearchResponse {
    String errorCode;
    List<Flight> flights;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", flights=" + flights +
                '}';
    }
}
