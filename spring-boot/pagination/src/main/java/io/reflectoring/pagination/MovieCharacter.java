package io.reflectoring.pagination;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
class MovieCharacter {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String movie;

}
