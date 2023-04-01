package com.reflectoring.userdetails.repository;

import com.reflectoring.userdetails.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginId(String loginId);

    List<User> findByIsInternalUser(boolean isInternalUser);

    List<User> findAll();
}
