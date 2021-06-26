/**
 * 
 */
package io.pratik.camelapp.models;

/**
 * @author pratikdas
 *
 */
public class Product {

	private String productName;
	private String productCategory;
	
	
	
	public Product(String productName, String productCategory) {
		super();
		this.productName = productName;
		this.productCategory = productCategory;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	
}
