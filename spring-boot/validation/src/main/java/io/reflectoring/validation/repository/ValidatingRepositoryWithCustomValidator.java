package io.reflectoring.validation.repository;

import org.springframework.data.repository.CrudRepository;

import io.reflectoring.validation.InputWithCustomValidator;

public interface ValidatingRepositoryWithCustomValidator extends CrudRepository<InputWithCustomValidator, Long> {
}
