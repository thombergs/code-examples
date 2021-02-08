package io.jgoerner.s3.application.port.in.object;

import io.jgoerner.s3.domain.Object;

import java.util.List;

public interface GetAllObjects {
  List<Object> getAllObjects(String space);
}
