package io.reflectoring.helper;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3BucketInitializer implements BeforeAllCallback {

	private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:3.3");
	private static final LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE)
			.withCopyFileToContainer(MountableFile.forClasspathResource("init-s3-bucket.sh", 0744), "/etc/localstack/init/ready.d/init-s3-bucket.sh")
			.withServices(Service.S3)
			.waitingFor(Wait.forLogMessage(".*Executed init-s3-bucket.sh.*", 1));

	// Bucket name as configured in src/test/resources/init-s3-bucket.sh
	private static final String BUCKET_NAME = "reflectoring-bucket";
	private static final Integer PRESIGNED_URL_VALIDITY = randomValiditySeconds();

	@Override
	public void beforeAll(final ExtensionContext context) {
		log.info("Creating localstack container : {}", LOCALSTACK_IMAGE);

		localStackContainer.start();
		addConfigurationProperties();

		log.info("Successfully started localstack container : {}", LOCALSTACK_IMAGE);
	}

	private void addConfigurationProperties() {
		System.setProperty("spring.cloud.aws.credentials.access-key", localStackContainer.getAccessKey());
		System.setProperty("spring.cloud.aws.credentials.secret-key", localStackContainer.getSecretKey());
		System.setProperty("spring.cloud.aws.s3.region", localStackContainer.getRegion());
		System.setProperty("spring.cloud.aws.s3.endpoint", localStackContainer.getEndpoint().toString());

		System.setProperty("io.reflectoring.aws.s3.bucket-name", BUCKET_NAME);
		System.setProperty("io.reflectoring.aws.s3.presigned-url.validity", String.valueOf(PRESIGNED_URL_VALIDITY));
	}

	private static int randomValiditySeconds() {
		return ThreadLocalRandom.current().nextInt(5, 11);
	}
	
	public static String bucketName() {
		return BUCKET_NAME;
	}

}