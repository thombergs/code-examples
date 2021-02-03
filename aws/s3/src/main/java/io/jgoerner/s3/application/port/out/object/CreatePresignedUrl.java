package io.jgoerner.s3.application.port.out.object;

import java.net.URL;

public interface CreatePresignedUrl {
  URL createURL(String bucket, String key, Long duration);
}
