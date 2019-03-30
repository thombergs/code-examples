package io.reflectoring.paging;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "character")
class MovieCharacter {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String movie;

}
