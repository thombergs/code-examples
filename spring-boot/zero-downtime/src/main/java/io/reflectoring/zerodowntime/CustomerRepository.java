package io.reflectoring.zerodowntime;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<OldCustomer, Long> {
}
