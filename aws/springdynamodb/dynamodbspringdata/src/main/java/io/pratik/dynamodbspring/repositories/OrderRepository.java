/**
 * 
 */
package io.pratik.dynamodbspring.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import io.pratik.dynamodbspring.models.Order;
import io.pratik.dynamodbspring.models.OrderID;

/**
 * @author pratikdas
 *
 */
@EnableScan
public interface OrderRepository  extends 
  CrudRepository<Order, OrderID> {
    
 
}
