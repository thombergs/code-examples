package io.jgoerner.s3.application.port.out.object;

import io.jgoerner.s3.domain.Object;

import java.io.InputStream;

public interface SaveObject {
  Object safe(String bucket, String key, String name, InputStream payload);
}
