/**
 * 
 */
package com.pratik.customers.models;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;



/**
 * @author Pratik Das
 *
 */

@Data
@Builder
public class Customer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private String dateOfBirth;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String gender;
	private String photo;
	private List<Address> addresses;
	private Credentials password;

}
