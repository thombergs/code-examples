package io.reflectoring.user.deactivation.scheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
class AwsConfiguration {

    private final AwsConfigurationProperties awsConfigurationProperties;

    @Bean
    public SchedulerClient schedulerClient() {
        final var accessKey = awsConfigurationProperties.getAccessKey();
        final var secretKey = awsConfigurationProperties.getSecretKey();
        final var regionName = awsConfigurationProperties.getEventbridgeScheduler().getRegion();

        final var credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SchedulerClient.builder()
            .region(Region.of(regionName))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    }

}