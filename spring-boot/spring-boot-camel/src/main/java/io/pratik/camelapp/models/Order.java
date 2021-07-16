/**
 * 
 */
package io.pratik.camelapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pratikdas
 *
 */
public class Order {
	
	private String orderNo;
	private String orderDate;
	
	private List<OrderLine> orderLines;
	
	private double totalDiscount;
	
	private double orderPrice;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public void addOrderLine(OrderLine orderLine) {
		if(orderLines == null) orderLines = new ArrayList<OrderLine>();
		orderLines.add(orderLine);
	}
	

}
