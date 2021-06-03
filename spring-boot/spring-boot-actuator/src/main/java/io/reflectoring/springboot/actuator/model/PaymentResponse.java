package io.reflectoring.springboot.actuator.model;

import io.reflectoring.springboot.actuator.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponse {
  String paymentTransactionId;
  PaymentStatus status;
}
