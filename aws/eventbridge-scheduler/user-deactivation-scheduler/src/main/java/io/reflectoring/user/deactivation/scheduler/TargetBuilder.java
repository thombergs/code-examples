package io.reflectoring.user.deactivation.scheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.services.scheduler.model.Target;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
class TargetBuilder {

    private final ObjectMapper objectMapper;
    private final AwsConfigurationProperties awsConfigurationProperties;

    public Target build(@NonNull final Object input) {
        final var targetProperties = awsConfigurationProperties.getEventbridgeScheduler().getTarget();
        return Target.builder()
            .arn(targetProperties.getArn())
            .roleArn(targetProperties.getRoleArn())
            .input(toJson(input))
            .build();
    }

    @SneakyThrows
    private String toJson(@NonNull final Object input) {
        return objectMapper.writeValueAsString(input);
    }

}