/**
 * 
 */
package io.pratik.springLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pratik Das
 *
 */
@RestController
public class UserController {
    static final Logger logger = LoggerFactory.getLogger(SpringLoggerApplication.class);

    private UserService userService;
    
    @Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}



	@GetMapping("/users/{userID}")
	public String getUser(@PathVariable("userID") final String userID) {
		
		MDC.put("user", userID);
		MDC.put("function", "userInquiry");
		
		logger.info("Controller: Fetching user with id {}", userID);
		
		MDC.remove("user");
		MDC.remove("function");
		
		return userService.getUser(userID);
		
	}
	

}
