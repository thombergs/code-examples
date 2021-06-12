/**
 * 
 */
package io.pratik.dynamodbspring.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import io.pratik.dynamodbspring.models.Customer;

/**
 * @author pratikdas
 *
 */
@EnableScan
public interface CustomerRepository  extends 
  CrudRepository<Customer, String> {
    
 
}
