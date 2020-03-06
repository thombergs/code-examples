package io.reflectoring.boundaries.billingmodule.api;

import lombok.Value;

@Value
public class Invoice {

  private Long userId;
  private double amount;

}
