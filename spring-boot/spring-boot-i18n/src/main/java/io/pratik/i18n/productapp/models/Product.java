/**
 * 
 */
package io.pratik.i18n.productapp.models;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author pratikdas
 *
 */
public class Product {
	
	private String name;
	private String lastUpdated;
	private String price;
	private String summary;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	

}
