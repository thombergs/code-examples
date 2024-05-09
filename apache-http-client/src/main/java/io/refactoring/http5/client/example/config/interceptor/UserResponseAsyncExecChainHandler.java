package io.refactoring.http5.client.example.config.interceptor;

import io.refactoring.http5.client.example.RequestProcessingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.AsyncExecCallback;
import org.apache.hc.client5.http.async.AsyncExecChain;
import org.apache.hc.client5.http.async.AsyncExecChainHandler;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.impl.BasicEntityDetails;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncDataConsumer;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;

/** The Custom response async chain handler. */
@Slf4j
public class UserResponseAsyncExecChainHandler implements AsyncExecChainHandler {
  @Override
  public void execute(
      HttpRequest httpRequest,
      AsyncEntityProducer asyncEntityProducer,
      AsyncExecChain.Scope scope,
      AsyncExecChain asyncExecChain,
      AsyncExecCallback asyncExecCallback)
      throws HttpException, IOException {
    try {
      boolean requestHandled = false;
      if (httpRequest.containsHeader("x-base-number")
          && httpRequest.containsHeader("x-req-exec-number")) {
        final String path = httpRequest.getPath();
        if (StringUtils.startsWith(path, "/api/users/")) {
          requestHandled = handleUserRequest(httpRequest, asyncExecCallback);
        }
      }
      if (!requestHandled) {
        asyncExecChain.proceed(httpRequest, asyncEntityProducer, scope, asyncExecCallback);
      }
    } catch (IOException | HttpException ex) {
      String msg = "Failed to execute request.";
      log.error(msg, ex);
      throw new RequestProcessingException(msg, ex);
    }
  }

  private boolean handleUserRequest(HttpRequest httpRequest, AsyncExecCallback asyncExecCallback)
      throws HttpException, IOException {
    boolean requestHandled = false;
    final Header baseNumberHeader = httpRequest.getFirstHeader("x-base-number");
    final String baseNumberStr = baseNumberHeader.getValue();
    int baseNumber = Integer.parseInt(baseNumberStr);

    final Header reqExecNumberHeader = httpRequest.getFirstHeader("x-req-exec-number");
    final String reqExecNumberStr = reqExecNumberHeader.getValue();
    int reqExecNumber = Integer.parseInt(reqExecNumberStr);

    // check if user id is multiple of base value
    if (reqExecNumber % baseNumber == 0) {
      final String reasonPhrase = "Multiple of " + baseNumber;
      final HttpResponse response = new BasicHttpResponse(HttpStatus.SC_OK, reasonPhrase);
      final ByteBuffer content = ByteBuffer.wrap(reasonPhrase.getBytes(StandardCharsets.US_ASCII));
      final BasicEntityDetails entityDetails =
          new BasicEntityDetails(content.remaining(), ContentType.TEXT_PLAIN);
      final AsyncDataConsumer asyncDataConsumer =
          asyncExecCallback.handleResponse(response, entityDetails);
      asyncDataConsumer.consume(content);
      asyncDataConsumer.streamEnd(null);
      requestHandled = true;
    }
    return requestHandled;
  }
}
