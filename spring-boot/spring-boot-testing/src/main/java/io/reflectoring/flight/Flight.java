package io.reflectoring.flight;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Flight {

  @Id
  @GeneratedValue
  private Long id;

  private String flightNumber;

  private String airline;

}
