package com.reflectoring.gymbuddy.dto.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import java.time.LocalDateTime;
import java.util.List;

public class SessionUpdateRequest {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime start;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime end;


  public SessionUpdateRequest(LocalDateTime start, LocalDateTime end){
    this.start = start;
    this.end = end;
  }

  private SessionUpdateRequest(SessionUpdateRequestBuilder builder){
    this.start = builder.start;
    this.end = builder.end;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }


  public class SessionUpdateRequestBuilder{
    private LocalDateTime start;
    private LocalDateTime end;

    public SessionUpdateRequestBuilder(){}

    public SessionUpdateRequestBuilder start(LocalDateTime start){
      this.start = start;
      return this;
    }

    public SessionUpdateRequestBuilder end(LocalDateTime end){
      this.end = end;
      return this;
    }

    public SessionUpdateRequest build(){
      return new SessionUpdateRequest(this);
    }
  }
}
