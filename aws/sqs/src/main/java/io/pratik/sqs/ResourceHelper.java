/**
 * 
 */
package io.pratik.sqs;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

/**
 * @author pratikdas
 *
 */
public class ResourceHelper {
	private static Logger logger = Logger.getLogger(ResourceHelper.class.getName());
	
	public static void main(String[] args) {
		// createStandardQueue();
		// createFifoQueue();
		createSNSTopicWithSubscription();
	}
	

	
	public static void createStandardQueue() {
		SqsClient sqsClient = getSQSClient();
		
		String dlqName = "mydlq";
		CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
									                .queueName(dlqName)
									                .build();
		

		// Create dead letter queue
		CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
		
		
		String dlqArn = getQueueArn(dlqName,"us-east-1"); 
		
		Map<QueueAttributeName, String> attributeMap = new HashMap<QueueAttributeName, String>();
		attributeMap.put(QueueAttributeName.REDRIVE_POLICY, 
				"{\"maxReceiveCount\":10,\"deadLetterTargetArn\":\""+dlqArn+"\"}");
	    
		// Prepare request for creating the standard queue
		createQueueRequest = CreateQueueRequest.builder()
                .queueName("myqueue")
                .attributes(attributeMap)
                .build();

		// create the queue
	    createQueueResponse = sqsClient.createQueue(createQueueRequest);
	    
	    logger.info("Queue URL " + createQueueResponse.queueUrl());
	}
	
	public static void createFifoQueue() {
		SqsClient sqsClient = getSQSClient();
		
		
		Map<QueueAttributeName, String> attributeMap = new HashMap<QueueAttributeName, String>();
		
		attributeMap.put(QueueAttributeName.FIFO_QUEUE, "true");
		attributeMap.put(QueueAttributeName.DEDUPLICATION_SCOPE, "messageGroup");
		attributeMap.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "false");
		
		CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName("myfifoqueue.fifo")
                .attributes(attributeMap )
                .build();

		CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        logger.info("url "+createQueueResponse.queueUrl());
	}
	
	public static void createSNSTopicWithSubscription() {
		SnsClient snsClient = getSNSClient();
		
		CreateTopicRequest createTopicRequest = CreateTopicRequest.builder().name("mytopic").build();
		CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest );
		
		String topicArn = createTopicResponse.topicArn();
		//Topic topic = Topic.builder().topicArn(topicArn).build();

		String queueArn= getQueueArn("myqueue","us-east-1");
		
		SubscribeRequest subscribeRequest = SubscribeRequest.builder()
															.protocol("sqs")
															.topicArn(topicArn)
															.endpoint(queueArn)
															.build();
		SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest );

	    
	    logger.info("subscriptionArn " + subscribeResponse.subscriptionArn());
	}
	

	
	private static SqsClient getSQSClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("pratikpoc");
		
		SqsClient sqsClient = SqsClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1).build();
		return sqsClient;
	}
	
	private static SnsClient getSNSClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("pratikpoc");
		
		SnsClient snsClient = SnsClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1).build();
		return snsClient;
	}
	

	
	private static String getQueueArn(final String queueName, final String region) {
		return "arn:aws:sqs:"+region + ":" + AppConfig.ACCOUNT_NO+ ":" + queueName;
	}

}
