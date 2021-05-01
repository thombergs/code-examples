/**
 * 
 */
package io.pratik.users;

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
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
}
