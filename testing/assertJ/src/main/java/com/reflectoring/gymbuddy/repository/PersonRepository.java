package com.reflectoring.gymbuddy.repository;

import com.reflectoring.gymbuddy.domain.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

  Optional<Person> findByEmail(String email);

  void deleteByEmail(String email);

}
