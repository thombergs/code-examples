/**
 * 
 */
package io.pratik;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.firehose.model.CreateDeliveryStreamRequest;
import software.amazon.awssdk.services.firehose.model.CreateDeliveryStreamResponse;
import software.amazon.awssdk.services.firehose.model.DeliveryStreamType;
import software.amazon.awssdk.services.firehose.model.ExtendedS3DestinationConfiguration;
import software.amazon.awssdk.services.firehose.model.KinesisStreamSourceConfiguration;
import software.amazon.awssdk.services.firehose.model.S3DestinationConfiguration;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;
import software.amazon.awssdk.services.kinesis.model.CreateStreamResponse;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamSummaryRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamSummaryResponse;
import software.amazon.awssdk.services.kinesis.model.ResourceNotFoundException;
import software.amazon.awssdk.services.kinesis.model.StreamDescriptionSummary;
import software.amazon.awssdk.services.kinesis.model.StreamMode;
import software.amazon.awssdk.services.kinesis.model.StreamModeDetails;
import software.amazon.awssdk.services.kinesis.model.StreamStatus;

/**
 * @author pratikdas
 *
 */
public class FirehoseResourceHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createDeliveryStream();

	}
	

	public static void createDeliveryStream() {
		 FirehoseClient firehoseClient = getFirehoseClient();
		 		
		 String kinesisStreamARN = "";
		 
		 String roleARN = "";
		 KinesisStreamSourceConfiguration kinesisStreamSourceConfiguration = 
				 KinesisStreamSourceConfiguration.builder().build();
		
		 String bucketARN = "";
		 ExtendedS3DestinationConfiguration s3DestinationConfiguration 
		    = ExtendedS3DestinationConfiguration.builder()
		    .bucketARN(bucketARN)
		    .build();
		
		 String streamName = "";
		 CreateDeliveryStreamRequest createDeliveryStreamRequest = 
				 CreateDeliveryStreamRequest
				 .builder()
				 .deliveryStreamName(streamName )
				 .deliveryStreamType(DeliveryStreamType.DIRECT_PUT)
				 .kinesisStreamSourceConfiguration(kinesisStreamSourceConfiguration )
				 .extendedS3DestinationConfiguration(s3DestinationConfiguration )
				 .build();
		 CreateDeliveryStreamResponse response = firehoseClient.createDeliveryStream(createDeliveryStreamRequest );
		 firehoseClient.close();
	}
	
	private static FirehoseClient getFirehoseClient() {
		AwsCredentialsProvider credentialsProvider = 
				ProfileCredentialsProvider.create(Constants.AWS_PROFILE_NAME);
		
		FirehoseClient firehoseClient = 
				FirehoseClient.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.US_EAST_1)
				.build();
	
		
		return firehoseClient;
	}

}
