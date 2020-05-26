package io.reflectoring.awshelloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloWorldController {

  private final UserRepository userRepository;

  HelloWorldController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/hello")
  String helloWorld(){

    Iterable<User> users = userRepository.findAll();

    return "Hello AWS! Successfully connected to the database!";
  }

}
