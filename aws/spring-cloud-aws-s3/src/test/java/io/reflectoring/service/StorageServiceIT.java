package io.reflectoring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import io.reflectoring.configuration.AwsS3BucketProperties;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@SpringBootTest
class StorageServiceIT {

	@Autowired
	private S3Template s3Template;

	@Autowired
	private StorageService storageService;

	@Autowired
	private AwsS3BucketProperties awsS3BucketProperties;

	private static final LocalStackContainer localStackContainer;

	// Bucket name as configured in src/test/resources/init-s3-bucket.sh
	private static final String BUCKET_NAME = "reflectoring-bucket";
	private static final Integer PRESIGNED_URL_VALIDITY = randomValiditySeconds();

	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.4"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-s3-bucket.sh", 0744), "/etc/localstack/init/ready.d/init-s3-bucket.sh")
				.withServices(Service.S3)
				.waitingFor(Wait.forLogMessage(".*Executed init-s3-bucket.sh.*", 1));
		localStackContainer.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
		registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
		registry.add("spring.cloud.aws.s3.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.s3.endpoint", localStackContainer::getEndpoint);

		registry.add("io.reflectoring.aws.s3.bucket-name", () -> BUCKET_NAME);
		registry.add("io.reflectoring.aws.s3.presigned-url.validity", () -> PRESIGNED_URL_VALIDITY);
	}

	@Test
	void shouldSaveFileSuccessfullyToBucket() {
		// Prepare test file to upload
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);

		// Invoke method under test
		storageService.save(fileToUpload);

		// Verify that the file is saved successfully in S3 bucket
		final var isFileSaved = s3Template.objectExists(BUCKET_NAME, key);
		assertThat(isFileSaved).isTrue();
	}

	@Test
	void saveShouldThrowExceptionForNonExistBucket() {
		// Prepare test file to upload
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);

		// Configure a non-existent bucket name
		final var nonExistingBucketName = RandomString.make(20).toLowerCase();
		awsS3BucketProperties.setBucketName(nonExistingBucketName);

		// Invoke method under test and assert exception
		final var exception = assertThrows(S3Exception.class, () -> storageService.save(fileToUpload));
		assertThat(exception.getCause()).hasCauseInstanceOf(NoSuchBucketException.class);
		
		// Reset the bucket name to the original value
		awsS3BucketProperties.setBucketName(BUCKET_NAME);
	}

	@Test
	@SneakyThrows
	void shouldFetchSavedFileSuccessfullyFromBucketForValidKey() {
		// Prepare test file and upload to S3 Bucket
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);
		storageService.save(fileToUpload);

		// Invoke method under test
		final var retrievedObject = storageService.retrieve(key);

		// Read the retrieved content and assert integrity
		final var retrievedContent = readFile(retrievedObject.getContentAsByteArray());
		assertThat(retrievedContent).isEqualTo(fileContent);
	}

	@Test
	void shouldDeleteFileFromBucketSuccessfully() {
		// Prepare test file and upload to S3 Bucket
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);
		storageService.save(fileToUpload);
		
		// Verify that the file is saved successfully in S3 bucket
		var isFileSaved = s3Template.objectExists(BUCKET_NAME, key);
		assertThat(isFileSaved).isTrue();

		// Invoke method under test
		storageService.delete(key);

		// Verify that file is deleted from the S3 bucket
		isFileSaved = s3Template.objectExists(BUCKET_NAME, key);
		assertThat(isFileSaved).isFalse();
	}

	@Test
	@SneakyThrows
	void shouldGeneratePresignedUrlToFetchStoredObjectFromBucket() {
		// Prepare test file and upload to S3 Bucket
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);
		storageService.save(fileToUpload);

		// Invoke method under test
		final var presignedUrl = storageService.generateViewablePresignedUrl(key);

		// Perform a GET request to the presigned URL
		final var restClient = RestClient.builder().build();
		final var responseBody = restClient.method(HttpMethod.GET).uri(URI.create(presignedUrl.toExternalForm()))
				.retrieve().body(byte[].class);

		// verify the retrieved content matches the expected file content.
		final var retrievedContent = new String(responseBody, StandardCharsets.UTF_8);
		assertThat(fileContent).isEqualTo(retrievedContent);
	}

	@Test
	@SneakyThrows
	void shouldGeneratePresignedUrlForUploadingObjectToBucket() {
		// Prepare test file to upload
		final var key = RandomString.make(10) + ".txt";
		final var fileContent = RandomString.make(50);
		final var fileToUpload = createTextFile(key, fileContent);

		// Invoke method under test
		final var presignedUrl = storageService.generateUploadablePresignedUrl(key);

		// Upload the test file using the presigned URL
		final var restClient = RestClient.builder().build();
		final var response = restClient.method(HttpMethod.PUT).uri(URI.create(presignedUrl.toExternalForm()))
				.body(fileToUpload.getBytes()).retrieve().toBodilessEntity();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Verify that the file is saved successfully in S3 bucket
		var isFileSaved = s3Template.objectExists(BUCKET_NAME, key);
		assertThat(isFileSaved).isTrue();
	}

	private String readFile(byte[] bytes) {
		final var inputStreamReader = new InputStreamReader(new ByteArrayInputStream(bytes));
		return new BufferedReader(inputStreamReader).lines().collect(Collectors.joining("\n"));
	}

	@SneakyThrows
	private MultipartFile createTextFile(final String fileName, final String content) {
		final var fileContentBytes = content.getBytes();
		final var inputStream = new ByteArrayInputStream(fileContentBytes);
		return new MockMultipartFile(fileName, fileName, "text/plain", inputStream);
	}
	
	private static int randomValiditySeconds() {
		return ThreadLocalRandom.current().nextInt(5, 11);
	}

}
