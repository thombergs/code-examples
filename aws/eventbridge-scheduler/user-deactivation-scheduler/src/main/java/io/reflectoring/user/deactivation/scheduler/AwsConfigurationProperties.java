package io.reflectoring.user.deactivation.scheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws")
class AwsConfigurationProperties {

    @NotBlank(message = "AWS access key must be configured")
    private String accessKey;

    @NotBlank(message = "AWS secret key must be configured")
    private String secretKey;

    @Valid
    private EventbridgeScheduler eventbridgeScheduler = new EventbridgeScheduler();

    @Getter
    @Setter
    @Validated
    public class EventbridgeScheduler {

        @NotBlank(message = "Eventbridge scheduler region must be configured")
        private String region;
        
        @NotBlank(message = "Eventbridge scheduler group name must be configured")
        private String groupName;

        @Valid
        private Target target = new Target();

        @Getter
        @Setter
        @Validated
        public class Target {

            @NotBlank(message = "Eventbridge scheduler target ARN must be configured")
            private String arn;

            @NotBlank(message = "Eventbridge scheduler role ARN must be configured")
            private String roleArn;

        }

    }

}