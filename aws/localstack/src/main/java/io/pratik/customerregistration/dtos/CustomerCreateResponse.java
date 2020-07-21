/**
 * 
 */
package io.pratik.customerregistration.dtos;

import lombok.Builder;
import lombok.Data;

/**
 * @author Pratik Das
 *
 */
@Data
@Builder
public class CustomerCreateResponse {
	
	private String customerID;
	private String error;
}
