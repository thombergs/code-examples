package com.reflectoring.beginnersguide.repository;

import com.reflectoring.beginnersguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
