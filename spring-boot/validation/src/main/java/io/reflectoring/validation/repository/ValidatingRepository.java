package io.reflectoring.validation.repository;

import org.springframework.data.repository.CrudRepository;

import io.reflectoring.validation.Input;

public interface ValidatingRepository extends CrudRepository<Input, Long> {
}
