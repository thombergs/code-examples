package io.jgoerner.s3.application.port.in.object;

import io.jgoerner.s3.domain.ObjectPartial;
import io.jgoerner.s3.domain.Object;

public interface UpdateObject {
  Object update(String space, String key, ObjectPartial updates);
}
