package io.reflectoring.springboot.actuator.model;

import io.reflectoring.springboot.actuator.enums.PaymentMode;
import lombok.Data;

@Data
public class PaymentRequest {
  String orderId;
  PaymentMode paymentMode;
  double amount;
}
