/**
 * 
 */
package io.pratik.camelapp.models;

/**
 * @author pratikdas
 *
 */
public class OrderLine {
		
	private Product product;
	
	private int numberOfUnits;
	
	private double price;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	

	
}
