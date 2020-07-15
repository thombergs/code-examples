package io.reflectoring.resilience4j.retry.services;

import io.reflectoring.resilience4j.retry.exceptions.SeatsUnavailableException;
import io.reflectoring.resilience4j.retry.model.BookingRequest;
import io.reflectoring.resilience4j.retry.model.BookingResponse;
import io.reflectoring.resilience4j.retry.services.failures.NoFailure;
import io.reflectoring.resilience4j.retry.services.failures.PotentialFailure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightBookingService {
    PotentialFailure potentialFailure = new NoFailure();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public BookingResponse bookFlight(BookingRequest request) throws SeatsUnavailableException {
        System.out.println("Booking flight; current time = " + LocalDateTime.now().format(formatter));
        potentialFailure.occur();

        if (request.getFlight().getFlightNumber().contains("765")) {
            potentialFailure.occur();
            throw new SeatsUnavailableException("No seats available");
        }
        // book seats on flight
        System.out.println("Flight booking successful");
        return new BookingResponse("success");
    }

    public void setPotentialFailure(PotentialFailure potentialFailure) {
        this.potentialFailure = potentialFailure;
    }
}