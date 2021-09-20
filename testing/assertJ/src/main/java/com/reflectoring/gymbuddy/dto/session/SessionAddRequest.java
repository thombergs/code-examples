package com.reflectoring.gymbuddy.dto.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import java.time.LocalDateTime;
import java.util.List;

public class SessionAddRequest {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime start;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime end;

  private List<WorkoutAddRequest> workouts;

  public SessionAddRequest(LocalDateTime start, LocalDateTime end, List<WorkoutAddRequest> workouts){
    this.start = start;
    this.end = end;
    this.workouts = workouts;
  }

  private SessionAddRequest(SessionAddRequestBuilder builder){
    this.start = builder.start;
    this.end = builder.end;
    this.workouts = builder.workouts;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public List<WorkoutAddRequest> getWorkouts() {
    return workouts;
  }

  public static class SessionAddRequestBuilder{
    private LocalDateTime start;
    private LocalDateTime end;
    private List<WorkoutAddRequest> workouts;

    public SessionAddRequestBuilder(){}

    public SessionAddRequestBuilder start(LocalDateTime start){
      this.start = start;
      return this;
    }

    public SessionAddRequestBuilder end(LocalDateTime end){
      this.end = end;
      return this;
    }

    public SessionAddRequestBuilder workouts(List<WorkoutAddRequest> workouts){
      this.workouts = workouts;
      return this;
    }

    public SessionAddRequest build(){
      return new SessionAddRequest(this);
    }
  }
}
