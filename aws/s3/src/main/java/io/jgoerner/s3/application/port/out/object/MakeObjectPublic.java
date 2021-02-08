package io.jgoerner.s3.application.port.out.object;

public interface MakeObjectPublic {
  void makePublic(String bucket, String key);
}
