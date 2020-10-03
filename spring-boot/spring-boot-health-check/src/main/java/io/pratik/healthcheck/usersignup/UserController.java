/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@PostMapping("users")
	public ResponseEntity<String> addUser(@RequestBody User user) {
		userService.addUser(user);
		BodyBuilder response = ResponseEntity.status(HttpStatus.CREATED);

		return response.build();
	}
	
	@PostMapping("users/activate")
	public ResponseEntity<String> activateUser(@RequestBody User user) {
		userService.addUser(user);
		BodyBuilder response = ResponseEntity.status(HttpStatus.OK);
		return response.build();
	}

}
