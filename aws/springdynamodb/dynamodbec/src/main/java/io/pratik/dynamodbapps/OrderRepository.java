/**
 * 
 */
package io.pratik.dynamodbapps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.pratik.dynamodbapps.models.Order;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * @author pratikdas
 *
 */
@Repository
public class OrderRepository {

	@Autowired
	private DynamoDbEnhancedClient dynamoDbenhancedClient;

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
		DynamoDbTable<Order> orderTable = dynamoDbenhancedClient.table("Order", TableSchema.fromBean(Order.class));
		return orderTable;
	}

	// Retrieve a single order item from the database
	public Order getOrder(final String customerID, final String orderID) {
		DynamoDbTable<Order> orderTable = getTable();
		// Construct the key with partition and sort key
		Key key = Key.builder().partitionValue(customerID).sortValue(orderID).build();

		Order order = orderTable.getItem(key);

		return order;
	}

	public void deleteOrder(final String customerID, final String orderID) {
		DynamoDbTable<Order> orderTable = getTable();

		Key key = Key.builder().partitionValue(customerID).sortValue(orderID).build();

		DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest
				.builder()
				.key(key)
				.build();
		
		orderTable.deleteItem(deleteRequest);
	}
	
	public PageIterable<Order> scanOrders(final String customerID, final String orderID) {
		DynamoDbTable<Order> orderTable = getTable();
		
		return orderTable.scan();
	}
	
	public PageIterable<Order> findOrdersByValue(final String customerID, final double orderValue) {
		DynamoDbTable<Order> orderTable = getTable();
				
        AttributeValue attributeValue = AttributeValue.builder()
                .n(String.valueOf(orderValue))
                .build();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":value", attributeValue);

        Expression expression = Expression.builder()
                .expression("orderValue > :value")
                .expressionValues(expressionValues)
                .build();

        // Create a QueryConditional object that is used in the query operation
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(customerID)
                        .build());

        // Get items in the Customer table and write out the ID value
        PageIterable<Order> results = orderTable.query(r -> r.queryConditional(queryConditional).filterExpression(expression));
    	return results;
	}

}
