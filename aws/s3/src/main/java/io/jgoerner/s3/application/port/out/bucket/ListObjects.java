package io.jgoerner.s3.application.port.out.bucket;

import io.jgoerner.s3.domain.Object;

import java.util.List;

public interface ListObjects {
  List<Object> listObjectsInBucket(String bucket);
}
