package io.jgoerner.s3.application.port.in.space;

import io.jgoerner.s3.domain.Space;

import java.util.List;

public interface GetAllSpaces {
  List<Space> getAll();
}
