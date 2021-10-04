package com.reflectoring.gymbuddy.services.implementation;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.domain.Session;
import com.reflectoring.gymbuddy.domain.Session.SessionBuilder;
import com.reflectoring.gymbuddy.dto.session.SessionAddRequest;
import com.reflectoring.gymbuddy.dto.session.SessionUpdateRequest;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import com.reflectoring.gymbuddy.repository.SessionRepository;
import com.reflectoring.gymbuddy.services.SessionService;
import com.reflectoring.gymbuddy.services.WorkoutService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

  private SessionRepository sessionRepository;

  private WorkoutService workoutService;

  public SessionServiceImpl(SessionRepository sessionRepository, WorkoutService workoutService){
    this.sessionRepository = sessionRepository;
    this.workoutService = workoutService;
  }

  @Override
  public Session add(Person person, SessionAddRequest request) {

    Session session = new SessionBuilder()
        .start(request.getStart())
        .end(request.getEnd())
        .person(person)
        .workouts(new ArrayList<>())
        .build();

    session = sessionRepository.save(session);

    for(WorkoutAddRequest workoutRequest : request.getWorkouts()){
      session.getWorkouts().add(workoutService.add(session, workoutRequest));
    }

    return session;

  }

  @Override
  public Session update(long id, SessionUpdateRequest request) {

    Optional<Session> session = sessionRepository.findById(id);
    if(session.isPresent()){
      session.get().setStart(request.getStart());
      session.get().setEnd(request.getEnd());

      return sessionRepository.save(session.get());
    }else{
      throw new RuntimeException();
    }

  }

  @Override
  public Session get(long id) {
    return sessionRepository.getById(id);
  }

  @Override
  public List<Session> getAll() {
    return sessionRepository.findAll();
  }

  @Override
  public List<Session> getForPerson(String email) {
    return sessionRepository.getAllByPersonEmail(email);
  }
}
