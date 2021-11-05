package com.reflectoring.gymbuddy.services;

import com.reflectoring.gymbuddy.domain.Set;
import com.reflectoring.gymbuddy.domain.Workout;
import com.reflectoring.gymbuddy.dto.set.SetAddRequest;
import com.reflectoring.gymbuddy.dto.set.SetUpdateRequest;
import java.util.List;

public interface SetService {

  Set add(Workout workout, SetAddRequest request);

  Set update(long id,SetUpdateRequest request);

  List<Set> getSetsOfWorkout(long workoutId);

  void delete(long id);

}
