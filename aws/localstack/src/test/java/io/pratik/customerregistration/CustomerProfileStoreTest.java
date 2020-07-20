package io.pratik.customerregistration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import cloud.localstack.Localstack;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import io.pratik.customerregistration.dtos.CustomerCreateRequest;
import io.pratik.customerregistration.dtos.CustomerDto;
import io.pratik.customerregistration.repositories.CustomerProfileStore;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

/**
 * @author Pratik Das
 *
 */
@Slf4j
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { "dynamodb"})
class CustomerProfileStoreTest {
	
	private static final Region region = Region.US_EAST_1;
	private static final String TABLE_NAME = "entities";
	
	private CustomerProfileStore customerProfileStore = null;

	@BeforeEach
	void setUp() throws Exception {
		customerProfileStore = new CustomerProfileStore(Localstack.INSTANCE.getEndpointDynamoDB());
		createTable();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreateCustomerProfile() {
		CustomerCreateRequest customerDto = CustomerCreateRequest.builder()
				                                           .firstName("pratik")
				                                           .lastName("das")
				                                           .phoneNumber("657576")
				                                           .email("prat@gmail.co")
				                                           .gender("M")
				                                           .build();
				                                           
		String customerID = customerProfileStore.createProfile(customerDto );
		log.info("customer ID {}", customerID);
		assertTrue(customerID != null, "Item created");
		CustomerDto dto = customerProfileStore.fetchProfile(customerID);
		assertEquals("pratik", dto.getFirstName(), "first name matched");
		assertEquals("das", dto.getLastName(), "last name matched");
	}
	
	private void createTable() {
		try {
			DynamoDbClient ddbClient = DynamoDbClient.builder().endpointOverride(new URI(Localstack.INSTANCE.getEndpointDynamoDB())).region(region).build();
			
			String key = "pk";
			CreateTableRequest createTableRequest = CreateTableRequest.builder()
		            .attributeDefinitions(AttributeDefinition.builder()
		                    .attributeName(key )
		                    .attributeType(ScalarAttributeType.S)
		                    .build())
		            .keySchema(KeySchemaElement.builder()
		                    .attributeName(key)
		                    .keyType(KeyType.HASH)
		                    .build())
		            .provisionedThroughput(ProvisionedThroughput.builder()
		                    .readCapacityUnits(10l)
		                    .writeCapacityUnits(10l)
		                    .build())
		            .tableName(TABLE_NAME)
		            .build();
			
			ddbClient.createTable(createTableRequest);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
