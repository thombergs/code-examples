package io.reflectoring.passwordencoding.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class CarResources {

  // we use this endpoint as authentication test
  @GetMapping("/cars")
  public Set<Car> cars() {
    return Set.of(
        Car.builder().name("vw").color("black").build(),
        Car.builder().name("bmw").color("white").build());
  }
}
