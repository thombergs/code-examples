package io.reflectoring.liquibase.adapter.datastore;

import io.reflectoring.liquibase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
