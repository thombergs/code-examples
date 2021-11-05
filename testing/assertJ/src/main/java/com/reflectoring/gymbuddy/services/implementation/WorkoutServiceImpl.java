package com.reflectoring.gymbuddy.services.implementation;

import com.reflectoring.gymbuddy.domain.Session;
import com.reflectoring.gymbuddy.domain.Workout;
import com.reflectoring.gymbuddy.domain.Workout.WorkoutBuilder;
import com.reflectoring.gymbuddy.dto.set.SetAddRequest;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import com.reflectoring.gymbuddy.repository.WorkoutRepository;
import com.reflectoring.gymbuddy.services.SetService;
import com.reflectoring.gymbuddy.services.WorkoutService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutServiceImpl implements WorkoutService {

  private WorkoutRepository workoutRepository;

  private SetService setService;

  public WorkoutServiceImpl(WorkoutRepository workoutRepository, SetService setService){
    this.workoutRepository = workoutRepository;
    this.setService = setService;
  }

  @Override
  @Transactional
  public Workout add(Session session, WorkoutAddRequest request) {
    Workout workout = new WorkoutBuilder()
        .session(session)
        .sets(new ArrayList<>())
        .build();
    workout = workoutRepository.save(workout);

    for(SetAddRequest setRequest : request.getSets()){
      workout.getSets().add(setService.add(workout, setRequest));
    }

    return workout;
  }

  @Override
  public List<Workout> getAll(long sessionId) {
    return workoutRepository.findAllBySession(sessionId);
  }

  @Override
  public Workout get(long id) {
    return workoutRepository.getById(id);
  }

  @Override
  public void delete(long id) {
    workoutRepository.deleteById(id);
  }
}
