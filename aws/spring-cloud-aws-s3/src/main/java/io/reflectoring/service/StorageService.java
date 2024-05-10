package io.reflectoring.service;

import java.net.URL;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import io.reflectoring.configuration.AwsS3BucketProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3BucketProperties.class)
public class StorageService {

	private final S3Template s3Template;
	private final AwsS3BucketProperties awsS3BucketProperties;

	@SneakyThrows
	public void save(@NonNull final MultipartFile file) {
		final var key = file.getOriginalFilename();
		final var bucketName = awsS3BucketProperties.getBucketName();

		s3Template.upload(bucketName, key, file.getInputStream());
	}

	public S3Resource retrieve(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		return s3Template.download(bucketName, objectKey);
	}

	public void delete(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		s3Template.deleteObject(bucketName, objectKey);
	}

	public URL generateViewablePresignedUrl(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		final var urlValidity = awsS3BucketProperties.getPresignedUrlValidity();

		return s3Template.createSignedGetURL(bucketName, objectKey, urlValidity);
	}

	public URL generateUploadablePresignedUrl(@NonNull final String objectKey) {
		final var bucketName = awsS3BucketProperties.getBucketName();
		final var urlValidity = awsS3BucketProperties.getPresignedUrlValidity();

		return s3Template.createSignedPutURL(bucketName, objectKey, urlValidity);
	}

}
