package io.refactoring.http5.client.example.config.interceptor;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.protocol.HttpContext;

/** The Custom http response interceptor. */
@Slf4j
public class CustomHttpResponseInterceptor implements HttpResponseInterceptor {
  @Override
  public void process(HttpResponse response, EntityDetails entity, HttpContext context)
      throws HttpException, IOException {
    log.debug("Got {} response from server.", response.getCode());
  }
}
