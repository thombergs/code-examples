package com.reflectoring.gymbuddy.repository;

import com.reflectoring.gymbuddy.domain.Workout;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout,Long> {
  List<Workout> findAllBySession(@Param("session") long id);
}
