package io.reflectoring.resilience4j.retry.model;

public class BookingRequest {
    String requestId;
    Flight flight;
    int seatCount;
    String seatClass;

    public BookingRequest(String requestId, Flight flight, int seatCount, String seatClass) {
        this.requestId = requestId;
        this.flight = flight;
        this.seatCount = seatCount;
        this.seatClass = seatClass;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
}