package com.reflectoring.begginersguide.repository;

import com.reflectoring.begginersguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
