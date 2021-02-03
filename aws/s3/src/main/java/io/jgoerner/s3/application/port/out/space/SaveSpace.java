package io.jgoerner.s3.application.port.out.space;

import io.jgoerner.s3.domain.Space;

public interface SaveSpace {
  Space save(Space name);
}
