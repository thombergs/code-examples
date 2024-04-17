package io.reflectoring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@RequiredArgsConstructor
public class BucketExistenceValidator implements ConstraintValidator<BucketExists, String> {

	private final S3Client s3Client;

	@Override
	public boolean isValid(final String bucketName, final ConstraintValidatorContext context) {
		try {
			s3Client.headBucket(request -> request.bucket(bucketName));
		} catch (final NoSuchBucketException exception) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}