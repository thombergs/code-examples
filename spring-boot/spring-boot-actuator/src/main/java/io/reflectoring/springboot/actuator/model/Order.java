package io.reflectoring.springboot.actuator.model;

import java.util.List;
import lombok.Data;

@Data
public class Order {
  String customerId;
  List<OrderLineItem> lineItems;

  public Order(String customerId, List<OrderLineItem> lineItems) {
    this.customerId = customerId;
    this.lineItems = lineItems;
  }
}
