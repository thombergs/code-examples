package io.reflectoring.springboot.actuator.services;

import io.reflectoring.springboot.actuator.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  @Autowired
  private PaymentRepository repository;

}
