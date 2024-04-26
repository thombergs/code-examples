package io.reflectoring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;

@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = Application.class)
class PubSubIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final LocalStackContainer localStackContainer;
	
	// as configured in initializing hook script 'provision-resources.sh' in src/test/resources
	private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:user-account-created";
	private static final String QUEUE_URL = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/dispatch-email-notification";
	
	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.4"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("provision-resources.sh", 0744), "/etc/localstack/init/ready.d/provision-resources.sh")
				.withServices(Service.SNS, Service.SQS)
				.waitingFor(Wait.forLogMessage(".*Successfully provisioned resources.*", 1));
		localStackContainer.start();
	}
	
	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
		registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
		
		registry.add("spring.cloud.aws.sns.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.sns.endpoint", localStackContainer::getEndpoint);
		registry.add("io.reflectoring.aws.sns.topic-arn", () -> TOPIC_ARN);

		registry.add("spring.cloud.aws.sqs.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.sqs.endpoint", localStackContainer::getEndpoint);
		registry.add("io.reflectoring.aws.sqs.queue-url", () -> QUEUE_URL);		
	}
	
	@Test
	@SneakyThrows
	void test(CapturedOutput output) {
		// prepare API request body to create user
		final var name = RandomString.make();
		final var emailId = RandomString.make() + "@reflectoring.io";
		final var password = RandomString.make();
		final var userCreationRequestBody = String.format("""
		{
			"name"   : "%s",
			"emailId"  : "%s",
			"password" : "%s"
		}
		""", name, emailId, password);
				
		// execute API request to create user
		final var userCreationApiPath = "/api/v1/users";
		mockMvc.perform(post(userCreationApiPath)
			.contentType(MediaType.APPLICATION_JSON)
			.content(userCreationRequestBody))
			.andExpect(status().isCreated());
		
		// assert that message has been published to SNS topic
		final var expectedPublisherLog = String.format("Successfully published message to topic ARN: %s", TOPIC_ARN);
		Awaitility.await().atMost(1, TimeUnit.SECONDS).until(() -> output.getAll().contains(expectedPublisherLog));
		
		// assert that message has been received by the SQS queue
		final var expectedSubscriberLog = String.format("Dispatching account creation email to %s on %s", name, emailId);
		Awaitility.await().atMost(1, TimeUnit.SECONDS).until(() -> output.getAll().contains(expectedSubscriberLog));
	}
	
}
