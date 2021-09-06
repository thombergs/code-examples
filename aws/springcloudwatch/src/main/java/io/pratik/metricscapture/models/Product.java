/**
 * 
 */
package io.pratik.metricscapture.models;

import lombok.Builder;
import lombok.Data;

/**
 * @author pratikdas
 *
 */
@Builder
@Data
public class Product {
	private String name;
	private Double price;

}
