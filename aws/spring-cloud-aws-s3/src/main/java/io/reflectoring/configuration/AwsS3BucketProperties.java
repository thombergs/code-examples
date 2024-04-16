package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.s3")
public class AwsS3BucketProperties {

	@NotBlank(message = "S3 bucket name must be configured")
	private String bucketName;

	@Valid
	private PresignedUrl presignedUrl = new PresignedUrl();

	@Getter
	@Setter
	@Validated
	public class PresignedUrl {

		@NotNull(message = "S3 presigned URL expiration time must be specified")
		@Positive(message = "S3 presigned URL expiration time must be a positive value")
		private Integer validity;

	}

}
