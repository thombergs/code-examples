package com.reflectoring.gymbuddy.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "set")
public class Set {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private long weight;

  private long reps;

  @ManyToOne
  @JoinColumn(name = "workout_id")
  Workout workout;

  public Set(){}

  public Set(long id, long weight, long reps){
    this.id = id;
    this.weight = weight;
    this.reps = reps;
  }

  private Set(SetBuilder builder){
    this.id = builder.id;
    this.weight = builder.weight;
    this.reps = builder.reps;
    this.workout = builder.workout;
  }

  public long getId() {
    return id;
  }

  public long getWeight() {
    return weight;
  }

  public long getReps() {
    return reps;
  }

  public void setWeight(long weight) {
    this.weight = weight;
  }

  public void setReps(long reps) {
    this.reps = reps;
  }

  public Workout getWorkout() {
    return workout;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Set set = (Set) o;
    return id == set.id && weight == set.weight && reps == set.reps;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, weight, reps);
  }

  public static class SetBuilder{
    private long id;
    private long weight;
    private long reps;
    private Workout workout;

    public SetBuilder(){}

    public SetBuilder id(long id){
      this.id = id;
      return this;
    }

    public SetBuilder weight(long weight){
      this.weight = weight;
      return this;
    }

    public SetBuilder reps(long reps){
      this.reps = reps;
      return this;
    }

    public SetBuilder workout(Workout workout){
      this.workout = workout;
      return this;
    }

    public Set build(){
      return new Set(this);
    }

  }
}
