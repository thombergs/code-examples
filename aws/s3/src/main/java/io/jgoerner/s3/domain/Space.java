package io.jgoerner.s3.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Space {
  String name;
  String bucket;
  Integer ttl;
}
