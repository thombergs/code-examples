package io.reflectoring.springcloudredis.repository;

import io.reflectoring.springcloudredis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
