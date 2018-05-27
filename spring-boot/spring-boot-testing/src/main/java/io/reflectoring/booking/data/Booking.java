package io.reflectoring.booking.data;

import javax.persistence.*;

import io.reflectoring.customer.data.Customer;
import io.reflectoring.flight.data.Flight;
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

  @Column
  private String flightNumber;

}
