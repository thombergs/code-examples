package io.reflectoring.springboot.actuator.controllers;

import io.micrometer.core.instrument.MeterRegistry;
import io.reflectoring.springboot.actuator.model.Order;
import io.reflectoring.springboot.actuator.model.OrderCreatedResponse;
import io.reflectoring.springboot.actuator.model.OrderLineItem;
import io.reflectoring.springboot.actuator.services.OrderService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  @Autowired
  private OrderService orderService;

  @Autowired
  private MeterRegistry registry;


  @GetMapping("/{customerId}/orders")
  public List<Order> getOrders(@PathVariable(value = "customerId") String customerId) {
    System.out.println("Get orders requested for customer: " + customerId);

    // Dummy order history for example purpose
    OrderLineItem orderLineItem1 = new OrderLineItem();
    orderLineItem1.setProductId(UUID.randomUUID().toString());
    orderLineItem1.setQuantity(10);

    OrderLineItem orderLineItem2 = new OrderLineItem();
    orderLineItem2.setProductId(UUID.randomUUID().toString());
    orderLineItem2.setQuantity(12);

    OrderLineItem orderLineItem3 = new OrderLineItem();
    orderLineItem3.setProductId(UUID.randomUUID().toString());
    orderLineItem3.setQuantity(5);


    OrderLineItem orderLineItem4 = new OrderLineItem();
    orderLineItem4.setProductId(UUID.randomUUID().toString());
    orderLineItem4.setQuantity(25);

    OrderLineItem orderLineItem5 = new OrderLineItem();
    orderLineItem5.setProductId(UUID.randomUUID().toString());
    orderLineItem5.setQuantity(20);

    OrderLineItem orderLineItem6 = new OrderLineItem();
    orderLineItem6.setProductId(UUID.randomUUID().toString());
    orderLineItem6.setQuantity(4);

    return Arrays.asList(new Order(customerId, Arrays.asList(orderLineItem1, orderLineItem2)),
        new Order(customerId, Arrays.asList(orderLineItem3)),
        new Order(customerId, Arrays.asList(orderLineItem4, orderLineItem5, orderLineItem6)));
  }

  @PostMapping("/{customerId}/orders")
  public OrderCreatedResponse placeOrder(@PathVariable(value = "customerId") String customerId,
      @RequestBody Order order) {
    System.out.println("Place order requested for customer: " + customerId);

    registry.counter("orders.placed.counter").increment();

    OrderCreatedResponse response = new OrderCreatedResponse();
    response.setOrderId(UUID.randomUUID().toString());
    response.setCustomerId(customerId);

    return response;
  }
}