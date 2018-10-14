package io.reflectoring.validation.repository;

import io.reflectoring.validation.Input;
import org.springframework.data.repository.CrudRepository;

public interface ValidatingRepository extends CrudRepository<Input, Long> {
}
