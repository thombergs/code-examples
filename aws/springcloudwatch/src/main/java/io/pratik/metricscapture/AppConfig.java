/**
 * 
 */
package io.pratik.metricscapture;

import java.time.Duration;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

/**
 * @author pratikdas
 *
 */
@Configuration
public class AppConfig {
	


	@Bean
	public CloudWatchAsyncClient cloudWatchAsyncClient() {
		return CloudWatchAsyncClient.builder().region(Region.US_EAST_1)
				.credentialsProvider(ProfileCredentialsProvider.create("pratikpoc")).build();
	}
	
	@Bean
	public MeterRegistry getMeterRegistry() {
		CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig();
		
		MeterRegistry meterRegistry = new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM,
				cloudWatchAsyncClient());
				
		return meterRegistry;
	}

	private CloudWatchConfig setupCloudWatchConfig() {
		CloudWatchConfig cloudWatchConfig = new CloudWatchConfig() {
			
			private Map<String, String> configuration
	          = Map.of("cloudwatch.namespace", "productsApp",
	                   "cloudwatch.step", Duration.ofMinutes(1).toString());
			
			@Override
			public String get(String key) {
				return configuration.get(key);
			}
		};
		return cloudWatchConfig;
	}

}
