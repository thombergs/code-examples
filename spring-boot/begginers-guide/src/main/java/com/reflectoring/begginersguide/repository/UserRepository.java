package com.reflectoring.begginersguide.repository;

import com.reflectoring.begginersguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
