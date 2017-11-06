package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

	private UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping(path = "/user-service/users")
	public ResponseEntity<IdObject> createUser(@RequestBody @Valid User user) {
		User savedUser = this.userRepository.save(user);
		return ResponseEntity
				.status(201)
				.body(new IdObject(savedUser.getId()));
	}

	@GetMapping(path = "/user-service/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userRepository.findOne(id));
	}


}
