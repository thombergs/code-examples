package io.jgoerner.s3.application.port.out.object;

public interface MakeObjectPrivate {
  void makePrivate(String bucket, String key);
}
