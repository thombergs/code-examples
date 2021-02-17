package io.jgoerner.s3.application.port.out.bucket;

public interface DeleteBucket {
  void delete(String bucket);
}
