package com.reflectoring.gymbuddy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "session")
public class Session {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime start;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime end;

  @OneToMany(mappedBy = "session", fetch = FetchType.EAGER)
  private List<Workout> workouts;

  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;

  public Session(){}
  public Session(long id, LocalDateTime start, LocalDateTime end, List<Workout> workouts, Person person) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.workouts = workouts;
    this.person = person;
  }

  private Session(SessionBuilder builder){
    this.id = builder.id;
    this.start = builder.start;
    this.end = builder.end;
    this.workouts = builder.workouts;
    this.person = builder.person;
  }

  public long getId() {
    return id;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public List<Workout> getWorkouts() {
    return workouts;
  }

  public Person getPerson() {
    return person;
  }

  public long getDurationInMinutes(){
    return Duration.between(start, end).toMinutes();
  }

  public static class SessionBuilder{
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Workout> workouts;
    private Person person;

    public SessionBuilder(){}

    public SessionBuilder id(long id){
      this.id = id;
      return this;
    }

    public SessionBuilder start(LocalDateTime start){
      this.start = start;
      return this;
    }

    public SessionBuilder end(LocalDateTime end){
      this.end = end;
      return this;
    }

    public SessionBuilder workouts(List<Workout> workouts){
      this.workouts = workouts;
      return this;
    }

    public SessionBuilder person(Person person){
      this.person = person;
      return this;
    }

    public Session build(){
      return new Session(this);
    }
  }
}
