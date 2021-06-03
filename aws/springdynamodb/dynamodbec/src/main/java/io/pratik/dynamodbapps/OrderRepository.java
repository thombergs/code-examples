/**
 * 
 */
package io.pratik.dynamodbapps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.pratik.dynamodbapps.models.Order;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * @author pratikdas
 *
 */
@Repository
public class OrderRepository {
	
	@Autowired
	private DynamoDbEnhancedClient dynamoDbenhancedClient ;
	
	private DynamoDbTable<Order> orderTable ;
	
	

	public OrderRepository() {
		super();
	}

	// Store this order item in the database
	public void save(final Order order) {
		DynamoDbTable<Order> orderTable = getTable();
		
		orderTable.putItem(order);
	}

	/**
	 * @return
	 */
	private DynamoDbTable<Order> getTable() {
		DynamoDbTable<Order> orderTable = 
				dynamoDbenhancedClient.table("Order",
						TableSchema.fromBean(Order.class));
		return orderTable;
	}

	// Retrieve a single order item from the database
	public Order getOrder(final String customerID, final String orderID) {
		DynamoDbTable<Order> orderTable = getTable();
		// Construct the key with partition and sort key
		Key key = Key.builder().partitionValue(customerID)
				               .sortValue(orderID)
				               .build();
		
		Order order = orderTable.getItem(key);
		
		return order;
	}

}
