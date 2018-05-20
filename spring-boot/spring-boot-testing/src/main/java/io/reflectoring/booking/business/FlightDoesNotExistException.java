package io.reflectoring.booking.business;

class FlightDoesNotExistException extends RuntimeException {

  FlightDoesNotExistException(Long flightId) {
    super(String.format("A flight with ID '%d' doesn't exist!", flightId));
  }

}
