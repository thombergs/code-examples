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
public class CustomerCreateRequest {
	
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String gender;
	private String photo;
	private List<AddressDto> addresses;	
}
