package io.reflectoring.resilience4j.springboot.model;

public class Flight {
    String flightNumber;
    String flightDate;
    String from;
    String to;

    public Flight() {
    }

    public Flight(String flightNumber, String flightDate, String from, String to) {
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.from = from;
        this.to = to;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", flightDate='" + flightDate + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}