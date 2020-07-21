/**
 * 
 */
package io.pratik.customerregistration.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author Pratik Das
 *
 */
@Data
@Builder
public class CustomerDto {
	private String customerID;
	private String userName;
	private String dateOfBirth;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String gender;
	private String photo;
	private List<AddressDto> addresses;
}
