package com.reflectoring.gymbuddy.domain;

import java.util.List;
import java.util.Objects;
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
@Table(name = "workout")
public class Workout {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @OneToMany(mappedBy = "workout", fetch = FetchType.EAGER)
  private List<Set> sets;

  @ManyToOne
  @JoinColumn(name = "session_id")
  private Session session;

  public Workout(){}

  public Workout(long id, List<Set> sets, Session session){
    this.id = id;
    this.sets = sets;
    this.session = session;
  }

  private Workout(WorkoutBuilder builder){
    this.id = builder.id;
    this.sets = builder.sets;
    this.session = builder.session;
  }

  public long getId() {
    return id;
  }

  public List<Set> getSets() {
    return sets;
  }

  public Session getSession() {
    return session;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Workout workout = (Workout) o;
    return id == workout.id && sets.equals(workout.sets) && session.equals(workout.session);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sets, session);
  }

  public static class WorkoutBuilder{
    private long id;
    private List<Set> sets;
    private Session session;

    public WorkoutBuilder(){}

    public WorkoutBuilder id(long id){
      this.id = id;
      return this;
    }

    public WorkoutBuilder sets(List<Set> sets){
      this.sets = sets;
      return this;
    }

    public WorkoutBuilder session(Session session){
      this.session = session;
      return this;
    }

    public Workout build(){
      return new Workout(this);
    }
  }
}
