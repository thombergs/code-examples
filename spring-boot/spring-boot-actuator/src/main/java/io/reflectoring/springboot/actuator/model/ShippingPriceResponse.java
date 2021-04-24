package io.reflectoring.springboot.actuator.model;

import lombok.Data;

@Data
public class ShippingPriceResponse {
  String stateCode;
  Double amount;
}
