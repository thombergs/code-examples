package io.refactoring.http5.client.example.async.helper;

import io.refactoring.http5.client.example.RequestProcessingException;
import java.io.IOException;
import java.nio.CharBuffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.message.StatusLine;

/** The Simple http character stream consumer. */
@Slf4j
public class SimpleCharResponseConsumer extends AbstractCharResponseConsumer<SimpleHttpResponse> {
  /** The Http get request. */
  private final SimpleHttpRequest httpRequest;

  private final StringBuilder responseBuilder = new StringBuilder();

  /** The Error message. */
  private final String errorMessage;

  /**
   * Instantiates a new Simple http response callback.
   *
   * @param httpRequest the http request
   * @param errorMessage the error message
   */
  public SimpleCharResponseConsumer(SimpleHttpRequest httpRequest, String errorMessage) {
    this.httpRequest = httpRequest;
    this.errorMessage = errorMessage;
  }

  @Override
  protected void start(HttpResponse httpResponse, ContentType contentType)
      throws HttpException, IOException {
    log.debug(httpRequest + "->" + new StatusLine(httpResponse));
    responseBuilder.setLength(0);
  }

  @Override
  protected SimpleHttpResponse buildResult() throws IOException {
    return SimpleHttpResponse.create(HttpStatus.SC_OK, responseBuilder.toString());
  }

  @Override
  protected int capacityIncrement() {
    return 0;
  }

  @Override
  protected void data(CharBuffer src, boolean endOfStream) throws IOException {
    while (src.hasRemaining()) {
      responseBuilder.append(src.get());
    }
    if (endOfStream) {
      log.debug(responseBuilder.toString());
    }
  }

  @Override
  public void failed(Exception ex) {
    log.error(httpRequest + "->" + ex);
    throw new RequestProcessingException(errorMessage, ex);
  }

  @Override
  public void releaseResources() {}
}
