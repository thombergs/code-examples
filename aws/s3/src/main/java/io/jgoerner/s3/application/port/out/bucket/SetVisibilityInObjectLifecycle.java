package io.jgoerner.s3.application.port.out.bucket;

public interface SetVisibilityInObjectLifecycle {
  void setVisibility(String bucket, Integer ttlInDays);
}
