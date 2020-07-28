/**
 * 
 */
package io.reflectoring.customerregistration.repositories;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.reflectoring.customerregistration.dtos.CustomerCreateRequest;
import io.reflectoring.customerregistration.dtos.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

/**
 * @author Pratik Das
 *
 */
@Slf4j
@Service
public class CustomerProfileStore {
	private static final String TABLE_NAME = "entities";
	private static final Region region = Region.US_EAST_1;
	
	private final String awsEndpoint;
	
	public CustomerProfileStore(@Value("${aws.local.endpoint:#{null}}") String awsEndpoint) {
		super();
		this.awsEndpoint = awsEndpoint;
	}

	private DynamoDbClient getDdbClient() {
		DynamoDbClient dynamoDB = null;;
		try {
			DynamoDbClientBuilder builder = DynamoDbClient.builder();
			// awsLocalEndpoint is set only in local environments
			if(awsEndpoint != null) {
				// override aws endpoint with localstack URL in dev environment
				builder.endpointOverride(new URI(awsEndpoint));
			}
			dynamoDB = builder.region(region).build();
		}catch(URISyntaxException ex) {
			log.error("Invalid url {}",awsEndpoint);
			throw new IllegalStateException("Invalid url "+awsEndpoint,ex);
		}
		return dynamoDB;
	}
	
	/**
	 * Store profile data in dynamodb table
	 * @param customerDto
	 * @param key 
	 * @return customer ID generated using uuid
	 */
	public CustomerDto fetchProfile( String customerID) {
		
        DynamoDbClient ddb = getDdbClient();
        
        Map<String, AttributeValue> attributeKey = new HashMap<>();
        String key = "CUSTOMER:"+customerID;
        attributeKey.put("pk", AttributeValue.builder().s(key).build());
        GetItemRequest getItemRequest = GetItemRequest.builder().tableName(TABLE_NAME).key(attributeKey).build();
        GetItemResponse getItemResponse = ddb.getItem(getItemRequest);
        Map<String, AttributeValue> responseAttributeMap = getItemResponse.item();
        return CustomerDto.builder()
              .customerID(customerID)
              .firstName(responseAttributeMap.get("fname").s())
              .lastName(responseAttributeMap.get("lname").s())
              .email(responseAttributeMap.get("email").s())
              .phoneNumber(responseAttributeMap.get("phone").s())
              .build();
	}
	
	/**
	 * Store profile data in dynamodb table
	 * @param customerDto
	 * @return customer ID generated using uuid
	 */
	public String createProfile(final CustomerCreateRequest customerDto) {
        DynamoDbClient ddb = getDdbClient();
        
        HashMap<String,AttributeValue> itemKey = new HashMap<String,AttributeValue>();

        String customerID = UUID.randomUUID().toString();
        String key = "CUSTOMER:"+customerID;
        itemKey.put("pk", AttributeValue.builder().s(key).build());

        HashMap<String,AttributeValueUpdate> updatedValues =
                new HashMap<String,AttributeValueUpdate>();

        // Update the column specified by name with updatedVal
        updatedValues.put("fname", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(customerDto.getFirstName()).build())
                .action(AttributeAction.PUT)
                .build());
        
        updatedValues.put("lname", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(customerDto.getLastName()).build())
                .action(AttributeAction.PUT)
                .build());
        
        updatedValues.put("phone", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(customerDto.getPhoneNumber()).build())
                .action(AttributeAction.PUT)
                .build());
        
        updatedValues.put("email", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(customerDto.getEmail()).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();
        
        try {
            ddb.updateItem(request);
        }  catch (DynamoDbException e) {
        }
        return customerID;
        
	}

}
