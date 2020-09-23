/**
 * 
 */
package io.reflectoring.customerregistration.repositories;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.reflectoring.customerregistration.dtos.CustomerCreateRequest;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * @author Pratik Das
 *
 */
@Service
@Slf4j
public class CustomerImageStore {
	private static final Region region = Region.US_EAST_1;
	private static final String BUCKET_NAME = "io.pratik.profileimages";
	
	private final String awsEndpoint;
	
	public CustomerImageStore(@Value("${aws.local.endpoint:#{null}}") String awsEndpoint) {
		super();
		this.awsEndpoint = awsEndpoint;
	}

	private S3Client getS3Client() {
		S3Client s3 = null;;
		try {
			S3ClientBuilder builder = S3Client.builder();
			// awsEndpoint is set only in local environments
			if(awsEndpoint != null) {
				// override aws endpoint with localstack URL in dev environment
				builder.endpointOverride(new URI(awsEndpoint));
			}
			s3 = builder.region(region).build();
		}catch(URISyntaxException ex) {
			log.error("Invalid url {}",awsEndpoint);
			throw new IllegalStateException("Invalid url "+awsEndpoint,ex);
		}
		return s3;
	}
	
	/**
	 * Fetch profile image from s3 bucket
	 * @param customerDto
	 * @param key 
	 * @return customer ID generated using uuid
	 */
	public void fetchProfileImage(final CustomerCreateRequest customerDto, String key) {
		S3Client s3 = getS3Client();
		if(s3 != null) {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build();
		    s3.getObject(getObjectRequest);
			s3.getObject(GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build(),
					ResponseTransformer.toFile(Paths.get("image"+key)));
		}
	}
	
	public void saveImage(final CustomerCreateRequest customerDto, final String imageKey) {
		S3Client s3 = getS3Client();
		if(s3 != null) {
			// Put Object
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKET_NAME).key(imageKey).build();
			RequestBody requestBody = RequestBody.fromString(customerDto.getPhoto());
			s3.putObject(putObjectRequest, requestBody);
		}
	}

	
}
