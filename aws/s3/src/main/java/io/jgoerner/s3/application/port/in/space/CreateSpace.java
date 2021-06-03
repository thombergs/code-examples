package io.jgoerner.s3.application.port.in.space;

import io.jgoerner.s3.domain.Space;

public interface CreateSpace {
  Space create(String name);
}
