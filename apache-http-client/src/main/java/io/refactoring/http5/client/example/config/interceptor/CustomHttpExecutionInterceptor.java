package io.refactoring.http5.client.example.config.interceptor;

import io.refactoring.http5.client.example.RequestProcessingException;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.ExecChain;
import org.apache.hc.client5.http.classic.ExecChainHandler;
import org.apache.hc.core5.http.*;

/** The Custom http request interceptor. */
@Slf4j
public class CustomHttpExecutionInterceptor implements ExecChainHandler {
  @Override
  public ClassicHttpResponse execute(ClassicHttpRequest classicHttpRequest, ExecChain.Scope scope, ExecChain execChain) throws IOException, HttpException {
    try {
      classicHttpRequest.setHeader("x-request-id", UUID.randomUUID().toString());
      classicHttpRequest.setHeader("x-api-key", "secret-key");

      final ClassicHttpResponse response = execChain.proceed(classicHttpRequest, scope);
      log.debug("Got {} response from server.", response.getCode());

      return response;
    } catch (IOException | HttpException ex) {
      String msg = "Failed to execute request.";
      log.error(msg, ex);
      throw new RequestProcessingException(msg, ex);
    }
  }
}
