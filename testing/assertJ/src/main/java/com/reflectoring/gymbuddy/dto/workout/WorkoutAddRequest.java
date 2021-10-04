package com.reflectoring.gymbuddy.dto.workout;

import com.reflectoring.gymbuddy.dto.set.SetAddRequest;
import java.util.List;

public class WorkoutAddRequest {

  private List<SetAddRequest> sets;

  public List<SetAddRequest> getSets(){
    return this.sets;
  }

  public WorkoutAddRequest(List<SetAddRequest> sets){
    this.sets = sets;
  }

  public WorkoutAddRequest(WorkoutAddRequestBuilder builder){
    this.sets = builder.sets;
  }

  public static class WorkoutAddRequestBuilder{
      private List<SetAddRequest> sets;

      public WorkoutAddRequestBuilder(){}

      public WorkoutAddRequestBuilder sets(List<SetAddRequest> sets){
        this.sets = sets;
        return this;
      }

      public WorkoutAddRequest build(){
        return new WorkoutAddRequest(this);
      }
  }

}
