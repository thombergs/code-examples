package io.reflectoring.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import io.reflectoring.validation.BucketExists;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Maps configuration values defined in the active {@code .yml} file to the
 * corresponding instance variables below. The configuration properties are
 * referenced within the application to interact with the provisioned AWS S3
 * Bucket.
 *
 * <p>
 * <b> Example YAML configuration: </b>
 *
 * <pre>
 * io:
 *   reflectoring:
 *     aws:
 *       s3:
 *         bucket-name: s3-bucket-name
 *         presigned-url:
 *           validity: url-validity-in-seconds
 * </pre>
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.s3")
public class AwsS3BucketProperties {

	@BucketExists
	@NotBlank(message = "S3 bucket name must be configured")
	private String bucketName;

	@Valid
	private PresignedUrl presignedUrl = new PresignedUrl();

	@Getter
	@Setter
	@Validated
	public class PresignedUrl {

		/**
		 * The validity period in <b>seconds</b> for the generated presigned URLs. The
		 * URLs would not be accessible post expiration.
		 */
		@NotNull(message = "S3 presigned URL validity must be specified")
		@Positive(message = "S3 presigned URL validity must be a positive value")
		private Integer validity;

	}

	public Duration getPresignedUrlValidity() {
		var urlValidity = this.presignedUrl.validity;
		return Duration.ofSeconds(urlValidity);
	}

}
