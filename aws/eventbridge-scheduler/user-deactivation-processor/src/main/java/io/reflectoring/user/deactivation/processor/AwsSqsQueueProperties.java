package io.reflectoring.user.deactivation.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "io.reflectoring.aws.sqs")
class AwsSqsQueueProperties {

    private String queueUrl;

}