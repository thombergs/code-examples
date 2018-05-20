package io.reflectoring.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Customer {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
}
