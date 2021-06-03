/**
 * 
 */
package io.pratik.dynamodbspring.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * @author pratikdas
 *
 */
@DynamoDBTable(tableName = "Order")
public class Order {
	private String customerID;
	private String orderSerial;
	private double orderValue;
    private Instant createdDate;
    
    @Id
	private OrderID orderID;
    
    
    
   public Order() {
		super();
	}
// private List<Product> products;
    
	public Order(OrderID orderID) {
		super();
		this.orderID = orderID;
	}
	/*
	 * public OrderID getOrderID() { return orderID; } public void
	 * setOrderID(OrderID orderID) { this.orderID = orderID; }
	 */
	@DynamoDBHashKey(attributeName = "customerID")
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
      this.customerID = customerID;
	}
	
	@DynamoDBRangeKey(attributeName = "orderID")
	public String getOrderSerial() {
		return orderSerial;
	}
	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}
	
	@DynamoDBAttribute
	public Instant getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}
	
	@DynamoDBAttribute
	public double getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}
	
	/*@DynamoDBAttribute
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}*/
}
