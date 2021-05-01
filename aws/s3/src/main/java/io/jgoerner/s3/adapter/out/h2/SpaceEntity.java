package io.jgoerner.s3.adapter.out.h2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpaceEntity {
  @Id private String name;
  private String bucket;
  private Integer ttl;
}
