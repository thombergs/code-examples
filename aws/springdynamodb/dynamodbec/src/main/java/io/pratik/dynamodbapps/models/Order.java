/**
 * 
 */
package io.pratik.dynamodbapps.models;

import java.time.Instant;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * @author pratikdas
 *
 */
@DynamoDbBean
public class Order {
	private String customerID;
	private String orderID;
	private double orderValue;
    private Instant createdDate;
    
    private List<Product> products;
    
    @DynamoDbPartitionKey
    @DynamoDbAttribute("CustomerID")
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
      this.customerID = customerID;
	}
	
	@DynamoDbSortKey
	@DynamoDbAttribute("OrderID")
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public Instant getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}
	public double getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
