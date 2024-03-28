package io.refactoring.http5.client.example.config.interceptor;

import java.io.IOException;
import java.util.UUID;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

/** The Custom http request interceptor. */
public class CustomHttpRequestInterceptor implements HttpRequestInterceptor {
  @Override
  public void process(HttpRequest request, EntityDetails entity, HttpContext context)
      throws HttpException, IOException {
    request.setHeader("x-request-id", UUID.randomUUID().toString());
    request.setHeader("x-api-key", "secret-key");
  }
}
