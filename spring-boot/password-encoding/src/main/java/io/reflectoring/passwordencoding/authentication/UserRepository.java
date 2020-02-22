package io.reflectoring.passwordencoding.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<UserCredentials, String> {

  UserCredentials findByUsername(String username);
}
