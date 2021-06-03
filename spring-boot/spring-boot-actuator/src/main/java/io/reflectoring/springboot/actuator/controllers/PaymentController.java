package io.reflectoring.springboot.actuator.controllers;

import io.reflectoring.springboot.actuator.enums.PaymentStatus;
import io.reflectoring.springboot.actuator.model.PaymentRequest;
import io.reflectoring.springboot.actuator.model.PaymentResponse;
import io.reflectoring.springboot.actuator.services.PaymentService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
  @Autowired
  private PaymentService paymentService;

  @PostMapping("/{orderId}/payment")
  public PaymentResponse processPayments(@PathVariable String orderId, @RequestBody PaymentRequest request) {
    System.out.println("Processing payment for order: " + orderId);

    PaymentResponse response = new PaymentResponse();
    response.setPaymentTransactionId(UUID.randomUUID().toString());
    response.setStatus(PaymentStatus.SUCCESS);

    return response;
  }
}