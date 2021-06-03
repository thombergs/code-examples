/**
 * 
 */
package io.pratik.graphqldemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pratik Das
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Manufacturer {
	private String id;
	private String name;
	private String address;
}
