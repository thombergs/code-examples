package com.reflectoring.gymbuddy.repository;

import com.reflectoring.gymbuddy.domain.Set;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRepository extends JpaRepository<Set,Long> {

  List<Set> findAllByWorkout(@Param("workout") long id);
}
