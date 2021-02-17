/**
 * 
 */
package io.pratik.users;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pratik Das
 *
 */
@RestController
public class UserController {
	
	@GetMapping("/users")
	public List<User> fetchUsers() {
		
		return Arrays.asList(User.builder().firstName("user1").build(),
				User.builder().firstName("user2").build(),User.builder().firstName("user3").build());
	}
	

}
