package com.reflectoring.gymbuddy.services;

import com.reflectoring.gymbuddy.domain.Session;
import com.reflectoring.gymbuddy.domain.Workout;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import java.util.List;

public interface WorkoutService {

  Workout add(Session session, WorkoutAddRequest request);

  List<Workout> getAll(long sessionId);

  Workout get(long id);

  void delete(long id);

}
