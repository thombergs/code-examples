/**
 * 
 */
package io.pratik.models;

import io.pratik.AbstractProduct;

/**
 * @author Pratik Das
 *
 */
public class GroceryProduct extends AbstractProduct{
	private Price mrp;
	private Price discountedPrice;
	
	
	public GroceryProduct() {
		super();
		mrp = new Price();
		discountedPrice = new Price();
	}
	public Price getMrp() {
		return mrp;
	}
	public void setMrp(Price mrp) {
		this.mrp = mrp;
	}
	public Price getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Price discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

}
