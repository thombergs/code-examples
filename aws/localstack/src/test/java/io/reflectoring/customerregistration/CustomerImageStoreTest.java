/**
 * 
 */
package io.reflectoring.customerregistration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import io.reflectoring.customerregistration.dtos.CustomerCreateRequest;
import io.reflectoring.customerregistration.repositories.CustomerImageStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import cloud.localstack.Localstack;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

/**
 * @author Pratik Das
 *
 */
@Slf4j
@ExtendWith(LocalstackDockerExtension.class)
@ActiveProfiles("local")
@LocalstackDockerProperties(services = { "s3" })
@Disabled(value = "Disabled because starting Docker containers like this does not run on GitHub Actions.")
class CustomerImageStoreTest {
	
	private static final Region region = Region.US_EAST_1;
	private static final String BUCKET_NAME = "io.pratik.profileimages";
	
	private CustomerImageStore customerImageStore = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		createBucket();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testStoreImage() {
		customerImageStore = new CustomerImageStore(Localstack.INSTANCE.getEndpointS3());
		String photo = "test image";
		CustomerCreateRequest customerDto = CustomerCreateRequest.builder().firstName("pratik").photo(photo ).build();
		String imageKey = customerDto.getFirstName()+System.currentTimeMillis();
		customerImageStore.saveImage(customerDto, imageKey );
		assertTrue(keyExistsInBucket(imageKey),"Object created");
	}
	
	private void createBucket() {
		URI localEndpoint = null;
		try {
			localEndpoint = new URI(Localstack.INSTANCE.getEndpointS3());
			S3Client s3 = S3Client.builder().endpointOverride(localEndpoint).region(region).build();
	        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(BUCKET_NAME).build();
			s3.createBucket(createBucketRequest );
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private boolean keyExistsInBucket(final String objectKey ) {
		URI localEndpoint = null;
		try {
			localEndpoint = new URI(Localstack.INSTANCE.getEndpointS3());
			S3Client s3 = S3Client.builder().endpointOverride(localEndpoint).region(region).build();
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(objectKey).build();
			s3.getObject(getObjectRequest);
			return true;
		} catch (URISyntaxException e) {
			log.error("Invalid url {}",localEndpoint);
		} catch(NoSuchKeyException ex) {
			log.error("Key does not exist {}", objectKey);
		}
		return false;
		
	}

}
