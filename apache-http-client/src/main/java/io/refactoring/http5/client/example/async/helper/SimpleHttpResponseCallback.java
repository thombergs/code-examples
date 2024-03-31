package io.refactoring.http5.client.example.async.helper;

import io.refactoring.http5.client.example.RequestProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.message.StatusLine;

import java.util.HashMap;
import java.util.Map;

/** The Simple http response callback. */
@Slf4j
public class SimpleHttpResponseCallback implements FutureCallback<SimpleHttpResponse> {
  /** The Http get request. */
  private final SimpleHttpRequest httpRequest;

  /** The Error message. */
  private final String errorMessage;

  /**
   * Instantiates a new Simple http response callback.
   *
   * @param httpRequest the http request
   * @param errorMessage the error message
   */
  public SimpleHttpResponseCallback(SimpleHttpRequest httpRequest, String errorMessage) {
    this.httpRequest = httpRequest;
    this.errorMessage = errorMessage;
  }

  @Override
  public void completed(final SimpleHttpResponse response) {
    log.debug(httpRequest + "->" + new StatusLine(response));
    log.debug("Got response: {}", response.getBody());
  }

  @Override
  public void failed(final Exception ex) {
    log.error(httpRequest + "->" + ex);
    throw new RequestProcessingException(errorMessage, ex);
  }

  @Override
  public void cancelled() {
    log.debug(httpRequest + " cancelled");
  }
}
