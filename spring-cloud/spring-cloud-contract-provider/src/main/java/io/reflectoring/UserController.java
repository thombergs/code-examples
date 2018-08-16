package io.reflectoring;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping(path = "/user-service/users/{id}")
	public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable long id) {
		User userFromDb = userRepository.findById(id).get();
		userFromDb.updateFrom(user);
		userFromDb = userRepository.save(userFromDb);
		return ResponseEntity.ok(userFromDb);
	}

	@GetMapping(path = "/user-service/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userRepository.findById(id).get());
	}


}
