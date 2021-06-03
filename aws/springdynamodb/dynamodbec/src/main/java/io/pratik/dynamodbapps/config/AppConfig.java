/**
 * 
 */
package io.pratik.dynamodbapps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * @author pratikdas
 *
 */
@Configuration
public class AppConfig {

	@Bean
	public DynamoDbClient getDynamoDbClient() {
		AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.builder().profileName("pratikpoc")
				.build();

		return DynamoDbClient.builder().region(Region.US_EAST_1).credentialsProvider(credentialsProvider).build();
	}
	
	@Bean
	public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
		return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDbClient())
                .build();
	}

}
