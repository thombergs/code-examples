package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.sns")
public class AwsSnsTopicProperties {

	@NotBlank(message = "SNS topic ARN must be configured")
	private String topicArn;

}
