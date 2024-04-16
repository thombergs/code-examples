package io.reflectoring.service;

import java.net.URL;
import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.reflectoring.configuration.AwsS3BucketProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3BucketProperties.class)
public class StorageService {
	
	private final S3Client s3Client;
	private final S3Presigner s3Presigner;
	private final AwsS3BucketProperties awsS3BucketProperties;
		
	@SneakyThrows
	public void save(@NonNull final MultipartFile file) {
		final var key = file.getOriginalFilename();
		final var requestBody = RequestBody.fromBytes(file.getBytes());
		final var bucketName = awsS3BucketProperties.getBucketName();

		s3Client.putObject(
				request -> request.key(key).bucket(bucketName),
				requestBody);
	}
	
	public ResponseInputStream<GetObjectResponse> retrieve(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		return s3Client.getObject(request -> request.key(objectKey).bucket(bucketName));
	}
	
	public void delete(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		s3Client.deleteObject(request -> request.key(objectKey).bucket(bucketName));
	}
	
	public URL generateViewablePresignedUrl(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		final var urlValidity = awsS3BucketProperties.getPresignedUrl().getValidity();
		final var urlValidityDuration = Duration.ofSeconds(urlValidity);

		final var getObjectRequest = GetObjectRequest.builder()
				.key(objectKey)
				.bucket(bucketName)
				.build();

		final var presignRequest = GetObjectPresignRequest.builder()
				.getObjectRequest(getObjectRequest)
				.signatureDuration(urlValidityDuration)
				.build();

		final var response = s3Presigner.presignGetObject(presignRequest);
		return response.url();
	}
	
	public URL generateUploadablePresignedUrl(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		final var urlValidity = awsS3BucketProperties.getPresignedUrl().getValidity();
		final var urlValidityDuration = Duration.ofSeconds(urlValidity);

		final var putObjectRequest = PutObjectRequest.builder()
				.key(objectKey)
				.bucket(bucketName)
				.build();

		final var presignRequest = PutObjectPresignRequest.builder()
				.putObjectRequest(putObjectRequest)
				.signatureDuration(urlValidityDuration)
				.build();

		final var response = s3Presigner.presignPutObject(presignRequest);
		return response.url();
	}
	
}
