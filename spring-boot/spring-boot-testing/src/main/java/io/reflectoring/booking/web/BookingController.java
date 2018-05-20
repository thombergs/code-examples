package io.reflectoring.booking.web;

import io.reflectoring.booking.business.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.reflectoring.booking.data.Booking;

@RestController
public class BookingController {

  private BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping("/booking")
  public ResponseEntity<BookingResultResource> bookFlight(
          @RequestParam("customerId") Long customerId,
          @RequestParam("flightId") Long flightId) {
    try {
      Booking booking = bookingService.bookFlight(customerId, flightId);
      BookingResultResource bookingResult = BookingResultResource.builder()
              .success(true)
              .build();
      return ResponseEntity.ok(bookingResult);
    } catch (Exception e) {
      BookingResultResource bookingResult = BookingResultResource.builder()
              .success(false)
              .build();
      return ResponseEntity.badRequest().body(bookingResult);
    }
  }

}
