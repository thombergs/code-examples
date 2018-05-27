package io.reflectoring.booking.web;

import io.reflectoring.booking.business.BookingService;
import io.reflectoring.booking.data.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

  private BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping("/booking")
  public ResponseEntity<BookingResultResource> bookFlight(
          @RequestParam("customerId") Long customerId,
          @RequestParam("flightNumber") String flightNumber) {
    Booking booking = bookingService.bookFlight(customerId, flightNumber);
    BookingResultResource bookingResult = BookingResultResource.builder()
            .success(true)
            .build();
    return ResponseEntity.ok(bookingResult);
  }

}
