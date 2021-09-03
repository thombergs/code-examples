/**
 * 
 */
package io.pratik.metricscapture.models;

import java.util.List;

import lombok.Builder;

/**
 * @author pratikdas
 *
 */
@Builder
public class Order {
	private List<Product> products;
	
	private String orderDate;
	

}
