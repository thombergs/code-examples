package io.reflectoring.booking.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.reflectoring.customer.Customer;
import io.reflectoring.flight.Flight;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Booking {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Flight flight;

}
