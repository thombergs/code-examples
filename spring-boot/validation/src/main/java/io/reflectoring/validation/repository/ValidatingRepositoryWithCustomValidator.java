package io.reflectoring.validation.repository;

import io.reflectoring.validation.InputWithCustomValidator;
import org.springframework.data.repository.CrudRepository;

public interface ValidatingRepositoryWithCustomValidator extends CrudRepository<InputWithCustomValidator, Long> {
}
