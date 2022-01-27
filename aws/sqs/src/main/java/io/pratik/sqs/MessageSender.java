/**
 * 
 */
package io.pratik.sqs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

/**
 * @author pratikdas
 *
 */
public class MessageSender {

	private static final String TRACE_ID_NAME = "trace-id";
	private static Logger logger = Logger.getLogger(MessageSender.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// sendMessage();
		// sendMessageToFifo();
		 sendMessageToSnsTopic();
	}
	public static void sendMessageToSnsTopic() {
		SnsClient snsClient = getSNSClient();
		
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
		// generates a UUID as the traceId
		String traceId = UUID.randomUUID().toString();
		// add traceId as a message attribute
		messageAttributes.put(TRACE_ID_NAME, MessageAttributeValue.builder().dataType("String").stringValue(traceId).build());
		final String topicArn = "arn:aws:sns:us-east-1:" + AppConfig.ACCOUNT_NO + ":mytopic";

		PublishRequest publishRequest = PublishRequest.builder().topicArn(topicArn).message("Test message published to topic").build();
		PublishResponse publishResponse = snsClient.publish(publishRequest);
		
		logger.info("message id: "+ publishResponse.messageId());
		
		snsClient.close();
	}
	

	public static void sendMessage() {
		SqsClient sqsClient = getSQSClient();
		
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
		// generates a UUID as the traceId
		String traceId = UUID.randomUUID().toString();
		// add traceId as a message attribute
		messageAttributes.put(TRACE_ID_NAME, MessageAttributeValue.builder().dataType("String").stringValue(traceId).build());
		
		final String queueURL = "https://sqs.us-east-1.amazonaws.com/" +AppConfig.ACCOUNT_NO + "/myqueue";
		SendMessageRequest sendMessageRequest = SendMessageRequest.builder().queueUrl(queueURL).messageBody("Test message")
				.messageAttributes(messageAttributes)
				.build();
		SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
		
		logger.info("message id: "+ sendMessageResponse.messageId() );
		
		sqsClient.close();
	}
	
	public static void sendMessageToFifo() {
		SqsClient sqsClient = getSQSClient();
		
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
		// generates a UUID as the traceId
		String traceId = UUID.randomUUID().toString();
		// add traceId as a message attribute
		messageAttributes.put(TRACE_ID_NAME, MessageAttributeValue.builder().dataType("String").stringValue(traceId).build());
		
		final String queueURL = "https://sqs.us-east-1.amazonaws.com/" +AppConfig.ACCOUNT_NO + "/myfifoqueue.fifo";
		
		
		List<String> dedupIds = List.of("dedupid1","dedupid2","dedupid3","dedupid2","dedupid1");
		
		String messageGroupId = "signup";
		
		List<String> messages = List.of(
								"My fifo message1",
								"My fifo message2",
								"My fifo message3",
								"My fifo message2",
								"My fifo message1");
		short loop = 0;
		for (String message : messages) {
			
			SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
					.queueUrl(queueURL)
					.messageBody(message)
					.messageAttributes(messageAttributes)
					.messageDeduplicationId(dedupIds.get(loop))
					.messageGroupId(messageGroupId)
					.build();
			
			SendMessageResponse sendMessageResponse = sqsClient
					.sendMessage(sendMessageRequest);
			
			logger.info("message id and sequence no.: "+ sendMessageResponse.messageId() + " | " + sendMessageResponse.sequenceNumber());
			
			logger.info("responseMetadata " + sendMessageResponse.responseMetadata());
			loop+=1;
		}

		
		sqsClient.close();
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

}
