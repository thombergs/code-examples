/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pratik Das
 *
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@Table(name="USERS")
public class User {
	
	@Id
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String status;
	private String profileURL;
	

	public User(String firstName, String lastName, String mobile, String email, String status, String profileURL) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.status = status;
		this.profileURL = profileURL==null?null:URLHelper.shortenURL(profileURL);
	}
	


}
