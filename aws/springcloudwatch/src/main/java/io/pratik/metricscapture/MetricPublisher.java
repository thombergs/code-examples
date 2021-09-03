/**
 * 
 */
package io.pratik.metricscapture;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pratik.metricscapture.models.MetricTag;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

/**
 * @author pratikdas
 *
 */
@Service
public class MetricPublisher {
	
	private CloudWatchAsyncClient cloudWatchAsyncClient;
	
	
    @Autowired
	public MetricPublisher(CloudWatchAsyncClient cloudWatchAsyncClient) {
		super();
		this.cloudWatchAsyncClient = cloudWatchAsyncClient;
	}



    public void putMetricData(final String nameSpace, 
            final String metricName, 
            final Double dataPoint,
            final List<MetricTag> metricTags) {

		try {
			
			List<Dimension> dimensions = metricTags
					.stream()
					.map((metricTag)->{
				         return Dimension
					         .builder()
					         .name(metricTag.getName())
					         .value(metricTag.getValue())
					         .build();
			}).collect(Collectors.toList());			
			
			// Set an Instant object
			String time = ZonedDateTime
			           .now(ZoneOffset.UTC)
			           .format(DateTimeFormatter.ISO_INSTANT);
			Instant instant = Instant.parse(time);
			
			MetricDatum datum = MetricDatum
			         .builder()
			         .metricName(metricName)
			         .unit(StandardUnit.NONE)
			         .value(dataPoint)
			         .timestamp(instant)
			         .dimensions(dimensions)
			         .build();
			
			PutMetricDataRequest request = 
			           PutMetricDataRequest
			           .builder()
			           .namespace(nameSpace)
			           .metricData(datum)
			           .build();
			
			cloudWatchAsyncClient.putMetricData(request);
		
		} catch (CloudWatchException e) {
		   System.err.println(e.awsErrorDetails().errorMessage());
		}
	}
}
