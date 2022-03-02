/**
 * 
 */
package io.pratik;

import java.util.List;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorResponse;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;

/**
 * @author pratikdas
 *
 */
public class EventConsumer {
    private static final Logger logger = Logger.getLogger(EventConsumer.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		receiveEvents();

	}
	
	public static void receiveEventsWithKCL() {}
	

	
	public static void receiveEvents() {
        KinesisClient kinesisClient = getKinesisClient();
        
		String shardId = "shardId-000000000001";
        
		GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder().streamName(Constants.MY_DATA_STREAM).shardId(shardId).shardIteratorType(ShardIteratorType.TRIM_HORIZON.name()).build();
		GetShardIteratorResponse getShardIteratorResponse = kinesisClient.getShardIterator(getShardIteratorRequest );
		String shardIterator = getShardIteratorResponse.shardIterator();
		logger.info("shardIterator " + shardIterator);
		
		while(shardIterator != null) {
			GetRecordsRequest getRecordsRequest = GetRecordsRequest.builder().shardIterator(shardIterator).limit(5).build();
			GetRecordsResponse getRecordsResponse = kinesisClient.getRecords(getRecordsRequest );
	        
			
			List<Record> records = getRecordsResponse.records();
			
			logger.info("count "+records.size());
			records.forEach(record->{
				byte[] dataInBytes = record.data().asByteArray();
				logger.info(new String(dataInBytes));
			});
			
			shardIterator = getRecordsResponse.nextShardIterator();
		}

		kinesisClient.close();
	}
	
	private static KinesisClient getKinesisClient() {
		AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create(Constants.AWS_PROFILE_NAME);
		
		KinesisClient kinesisClient = KinesisClient
				.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1)
				.build();
		return kinesisClient;
	}

}
