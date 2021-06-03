package io.reflectoring.springboot.actuator.controllers;

import io.reflectoring.springboot.actuator.model.ShippingPriceResponse;
import io.reflectoring.springboot.actuator.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingController {
  @Autowired
  private ShippingService shippingService;

  @GetMapping("/shipping-price")
  public ShippingPriceResponse getShippingPrice(@RequestParam(value = "stateCode") String stateCode) {
    System.out.println("Shipping price API called");
     ShippingPriceResponse response = new ShippingPriceResponse();
     response.setAmount(shippingService.getShippingPriceByState(stateCode));
     response.setStateCode(stateCode);
     return response;
  }

  @EventListener
  void preloadCaches(ContextRefreshedEvent event) {
    System.out.println("Preloading caches");
    System.out.println(shippingService.getStates());
    System.out.println(shippingService.getShippingPriceByState("TX"));
    System.out.println(shippingService.getShippingPriceByState("VA"));
  }
}