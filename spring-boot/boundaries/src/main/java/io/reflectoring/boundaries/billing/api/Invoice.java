package io.reflectoring.boundaries.billing.api;

import lombok.Value;

@Value
public class Invoice {

  private Long userId;
  private double amount;

}
