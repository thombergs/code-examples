/**
 * 
 */
package io.pratik;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
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
public class DataStreamResourceHelper {

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createDataStream();

	}
	

	public static void createDataStream() {
		 KinesisClient kinesisClient = getKinesisClient();
		 
		 CreateStreamRequest createStreamRequest = CreateStreamRequest.builder().streamName(Constants.MY_DATA_STREAM).streamModeDetails(StreamModeDetails.builder().streamMode(StreamMode.ON_DEMAND).build()).build();
		 CreateStreamResponse createStreamResponse = kinesisClient.createStream(createStreamRequest);
		 
		 DescribeStreamSummaryRequest describeStreamSummaryRequest = DescribeStreamSummaryRequest.builder().streamName(Constants.MY_DATA_STREAM ).build();
		 DescribeStreamSummaryResponse describeStreamSummaryResponse =  kinesisClient.describeStreamSummary(describeStreamSummaryRequest );
	
		 
		 long startTime = System.currentTimeMillis();
		 long endTime = startTime + ( 10 * 60 * 1000 );
		 while ( System.currentTimeMillis() < endTime ) {
			  try {
			    Thread.sleep(20 * 1000);
			  } 
			  catch ( Exception e ) {}
			  
			  try {
					 StreamDescriptionSummary streamDescSumm = describeStreamSummaryResponse.streamDescriptionSummary();
			  
					 if(streamDescSumm.streamStatus().equals(StreamStatus.ACTIVE)) break;
					  try {
					      Thread.sleep( 1000 );
					  }catch ( Exception e ) {}
			  }catch ( ResourceNotFoundException e ) {}
			  
			  
		 }
		 
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
