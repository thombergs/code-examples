/**
 * 
 */
package io.pratik.springLogger;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@Service
@Slf4j
public class UserService {

	public String getUser(final String userID) {
		log.info("Service: Fetching user with id {}", userID);
		
		return "user with id "+userID;
		
	}
	
	

}
