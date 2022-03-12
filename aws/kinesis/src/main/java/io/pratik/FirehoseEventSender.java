/**
 * 
 */
package io.pratik;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.firehose.model.PutRecordRequest;
import software.amazon.awssdk.services.firehose.model.PutRecordResponse;
import software.amazon.awssdk.services.firehose.model.Record;

/**
 * @author pratikdas
 *
 */
public class FirehoseEventSender {
	private final static Logger logger = Logger.getLogger(FirehoseEventSender.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FirehoseEventSender().sendEvent();

	}
	
	public void sendEvent() {
		String deliveryStreamName= "PUT-S3-5ZGgA";
		
		String data = "Test data" + "\n";
		;
		Record record = Record.builder().data(SdkBytes.fromByteArray(data.getBytes())).build();
		
		PutRecordRequest putRecordRequest = PutRecordRequest
				.builder()
				.deliveryStreamName(deliveryStreamName).record(record).build();

		FirehoseClient firehoseClient = getFirehoseClient();
		// Put record into the DeliveryStream
		PutRecordResponse putRecordResponse = firehoseClient.putRecord(putRecordRequest);
		
		logger.info("record ID:: " + putRecordResponse.recordId());
		firehoseClient.close();
	}
	
	private static FirehoseClient getFirehoseClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create(Constants.AWS_PROFILE_NAME);
		
		FirehoseClient kinesisClient = FirehoseClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Constants.AWS_REGION).build();
		return kinesisClient;
	}
	

}
