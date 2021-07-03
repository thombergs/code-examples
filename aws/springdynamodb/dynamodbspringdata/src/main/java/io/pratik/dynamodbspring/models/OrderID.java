/**
 * 
 */
package io.pratik.dynamodbspring.models;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

/**
 * @author pratikdas
 *
 */
public class OrderID implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String customerID;
	private String orderSerial;
	
	public OrderID() {
		super();
		this.customerID = null;
		this.orderSerial = null;
	}
	
	public OrderID(String customerID, String orderSerial) {
		super();
		this.customerID = customerID;
		this.orderSerial = orderSerial;
	}
	
	@DynamoDBHashKey
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	@DynamoDBRangeKey
	public String getOrderSerial() {
		return orderSerial;
	}
	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}
	
	
}
