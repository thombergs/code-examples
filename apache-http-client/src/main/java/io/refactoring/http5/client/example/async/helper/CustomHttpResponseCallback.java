package io.refactoring.http5.client.example.async.helper;

import io.refactoring.http5.client.example.RequestProcessingException;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.message.StatusLine;

/** The http response callback. */
@Slf4j
public class CustomHttpResponseCallback implements FutureCallback<SimpleHttpResponse> {
  /** The Http get request. */
  private final SimpleHttpRequest httpRequest;

  /** The Error message. */
  private final String errorMessage;

  /** The Latch. */
  private final CountDownLatch latch;

  /**
   * Instantiates a new pipelined http response callback.
   *
   * @param httpRequest the http request
   * @param errorMessage the error message
   * @param latch the latch
   */
  public CustomHttpResponseCallback(
      SimpleHttpRequest httpRequest, String errorMessage, CountDownLatch latch) {
    this.httpRequest = httpRequest;
    this.errorMessage = errorMessage;
    this.latch = latch;
  }

  @Override
  public void completed(final SimpleHttpResponse response) {
    latch.countDown();
    log.debug(httpRequest + "->" + new StatusLine(response));
    log.debug("Got response: {}", response.getBody());
  }

  @Override
  public void failed(final Exception ex) {
    latch.countDown();
    log.error(httpRequest + "->" + ex);
    throw new RequestProcessingException(errorMessage, ex);
  }

  @Override
  public void cancelled() {
    latch.countDown();
    log.debug(httpRequest + " cancelled");
  }
}
