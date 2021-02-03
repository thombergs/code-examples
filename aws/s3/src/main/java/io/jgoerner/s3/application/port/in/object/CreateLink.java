package io.jgoerner.s3.application.port.in.object;

import java.net.URL;

public interface CreateLink {
  URL createLink(String space, String key, Long duration);
}
