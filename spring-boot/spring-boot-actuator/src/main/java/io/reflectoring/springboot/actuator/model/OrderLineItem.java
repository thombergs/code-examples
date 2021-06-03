package io.reflectoring.springboot.actuator.model;

import lombok.Data;

@Data
public class OrderLineItem {
  String productId;
  Integer quantity;
}
