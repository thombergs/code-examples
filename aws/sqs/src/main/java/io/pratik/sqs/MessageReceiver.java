/**
 * 
 */
package io.pratik.sqs;

import java.util.List;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

/**
 * @author pratikdas
 *
 */
public class MessageReceiver {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// receiveMessage();
		receiveFifoMessage();

	}
	
	public static void receiveMessage() {
		SqsClient sqsClient = getSQSClient();
		final String queueURL = "https://sqs.us-east-1.amazonaws.com/" +AppConfig.ACCOUNT_NO + "/myqueue";

		// long polling and wait for waitTimeSeconds before timed out
		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
		        .queueUrl(queueURL)
		        .waitTimeSeconds(20)
		        .messageAttributeNames("trace-id") // returns the trace Id
		        .build();
		List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
	}
	
	public static void receiveFifoMessage() throws InterruptedException {
		SqsClient sqsClient = getSQSClient();
		final String queueURL = "https://sqs.us-east-1.amazonaws.com/" +AppConfig.ACCOUNT_NO + "/myfifoqueue.fifo";

		// long polling and wait for waitTimeSeconds before timed out
		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
		        .queueUrl(queueURL)
		        .waitTimeSeconds(20)
		        .messageAttributeNames("trace-id") // returns the trace Id
		        .build();
		
		while(true) {
		
			Thread.sleep(20000l);
			List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
		    messages.stream().forEach(msg->{
		    	System.out.println(msg.body() + " " + msg.attributesAsStrings());
		    	
		    	String receiptHandle = msg.receiptHandle();
		    	DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueURL).receiptHandle(receiptHandle).build();
		    	DeleteMessageResponse deleteMessageResponse = sqsClient.deleteMessage(deleteMessageRequest );
		    	
		    	// logger.info(deleteMessageResponse.responseMetadata());
		    });
		
		}
	    
	
	}
	
	private static SqsClient getSQSClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("pratikpoc");
		
		SqsClient sqsClient = SqsClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1).build();
		return sqsClient;
	}

}
