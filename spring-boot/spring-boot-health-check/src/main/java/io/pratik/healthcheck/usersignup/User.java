/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name="USERS")
public class User {
	
	@Id
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String status;

}
