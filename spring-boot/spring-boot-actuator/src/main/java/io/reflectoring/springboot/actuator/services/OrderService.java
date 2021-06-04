package io.reflectoring.springboot.actuator.services;

import io.reflectoring.springboot.actuator.repositories.OrderRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @PostConstruct
  void loadCache() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
