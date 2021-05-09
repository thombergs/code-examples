/**
 * 
 */
package io.pratik.springcloudsqs.models;

import lombok.Data;

/**
 * @author pratikdas
 *
 */
@Data
public class SignupEvent {
	
	private String signupTime;
	private String userName;
	private String email;

}
