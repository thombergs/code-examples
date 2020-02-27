package io.reflectoring.passwordencoding.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
class CarResources {

  @GetMapping("/cars")
  public Set<Car> cars() {
    return Set.of(new Car("vw", "black"), new Car("bmw", "white"));
  }
}
