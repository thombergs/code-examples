package io.jgoerner.s3.application.port.out.space;

import io.jgoerner.s3.domain.Space;

import java.util.List;

public interface RetrieveAllSpaces {
  List<Space> findAll();
}
