package io.jgoerner.s3.application.port.out.bucket;

public interface RemoveVisibilityInObjectLifecycle {
  void removeVisibility(String bucket);
}
