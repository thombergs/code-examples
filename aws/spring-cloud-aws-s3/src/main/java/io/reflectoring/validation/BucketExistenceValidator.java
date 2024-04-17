package io.reflectoring.validation;

import io.awspring.cloud.s3.S3Template;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BucketExistenceValidator implements ConstraintValidator<BucketExists, String> {

	private final S3Template s3Template;

	@Override
	public boolean isValid(final String bucketName, final ConstraintValidatorContext context) {
		return s3Template.bucketExists(bucketName);
	}

}