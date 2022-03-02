/**
 * 
 */
package io.pratik;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;
import software.amazon.awssdk.services.kinesis.model.PutRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordsRequestEntry;
import software.amazon.awssdk.services.kinesis.model.PutRecordsResponse;

/**
 * @author pratikdas
 *
 */
public class EventSender {

    private static final Logger logger = Logger.getLogger(EventSender.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendEvent();

	}
	
	public static void sendEvent() {
        KinesisClient kinesisClient = getKinesisClient();
        
		String partitionKey = String.format("partitionKey-%d", 1);
		SdkBytes data = SdkBytes.fromByteBuffer(ByteBuffer.wrap("Test data".getBytes()));
		PutRecordRequest putRecordRequest 
		   = PutRecordRequest
			   .builder()
			   .streamName(Constants.MY_DATA_STREAM)
			   .partitionKey(partitionKey)
			   .data(data)
			   .build();
		
		 PutRecordResponse putRecordResult 
		 = kinesisClient.putRecord(putRecordRequest);
        
		 logger.info("Put Result" + putRecordResult);
         kinesisClient.close();
	}
	
	public static void sendEvents() {
        KinesisClient kinesisClient = getKinesisClient();
        
		String partitionKey = String.format("partitionKey-%d", 1);
        
 
        List <PutRecordsRequestEntry> putRecordsRequestEntryList  = new ArrayList<>(); 
        for (int i = 0; i < 5; i++) {
        	SdkBytes data = SdkBytes
        			.fromByteBuffer(ByteBuffer.wrap(("Test event "+i).getBytes()));
        	
            PutRecordsRequestEntry putRecordsRequestEntry  
                    = PutRecordsRequestEntry.builder()
                    
                    .data(data)
                    .partitionKey(partitionKey)
                    .build();
            
            putRecordsRequestEntryList.add(putRecordsRequestEntry); 
        }
        

        PutRecordsRequest putRecordsRequest 
						        = PutRecordsRequest
						        .builder()
						        .streamName(Constants.MY_DATA_STREAM)
						        .records(putRecordsRequestEntryList)
						        .build();
        
		PutRecordsResponse putRecordsResult = kinesisClient
				.putRecords(putRecordsRequest);
		
        logger.info("Put Result" + putRecordsResult);
        kinesisClient.close();
	}
	
	private static KinesisClient getKinesisClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create(Constants.AWS_PROFILE_NAME);
		
		KinesisClient kinesisClient = KinesisClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1).build();
		return kinesisClient;
	}

}
