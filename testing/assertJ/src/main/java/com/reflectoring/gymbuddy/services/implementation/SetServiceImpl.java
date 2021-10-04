package com.reflectoring.gymbuddy.services.implementation;

import com.reflectoring.gymbuddy.domain.Set;
import com.reflectoring.gymbuddy.domain.Set.SetBuilder;
import com.reflectoring.gymbuddy.domain.Workout;
import com.reflectoring.gymbuddy.dto.set.SetAddRequest;
import com.reflectoring.gymbuddy.dto.set.SetUpdateRequest;
import com.reflectoring.gymbuddy.repository.SetRepository;
import com.reflectoring.gymbuddy.services.SetService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SetServiceImpl implements SetService {
  private SetRepository setRepository;

  public SetServiceImpl(SetRepository setRepository){
    this.setRepository = setRepository;
  }
  @Override
  public Set add(Workout workout, SetAddRequest request) {
    Set set = new SetBuilder()
        .reps(request.getReps())
        .weight(request.getWeight())
        .workout(workout)
        .build();

    set = setRepository.save(set);
    return set;
  }

  @Override
  public Set update(long id,SetUpdateRequest request) {
    Optional<Set> set = setRepository.findById(id);
    if(set.isPresent()){
        set.get().setWeight(request.getWeight());
        set.get().setReps(request.getReps());
        return setRepository.save(set.get());
    }else{
      throw new RuntimeException();
    }
  }

  @Override
  public List<Set> getSetsOfWorkout(long workoutId) {

    return setRepository.findAllByWorkout(workoutId);

  }

  @Override
  public void delete(long id) {
    setRepository.deleteById(id);
  }
}
