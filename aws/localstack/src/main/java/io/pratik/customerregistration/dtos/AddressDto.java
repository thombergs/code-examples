/**
 * 
 */
package io.pratik.customerregistration.dtos;

import lombok.Data;

/**
 * @author Pratik Das
 *
 */
@Data
public class AddressDto {
	private String premiseNumber;
	private String streetName;
	private String city;
	private String countryName;
	private String zip;

}
