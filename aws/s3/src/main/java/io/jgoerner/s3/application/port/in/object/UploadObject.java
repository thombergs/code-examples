package io.jgoerner.s3.application.port.in.object;

import io.jgoerner.s3.domain.Object;

import java.io.InputStream;

public interface UploadObject {
  Object upload(String space, String name, InputStream payload);
}
