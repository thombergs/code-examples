package io.refactoring.http5.client.example;

/** Represents an exception for HTTP request processing. */
public class RequestProcessingException extends RuntimeException {
  /**
   * Construction.
   *
   * @param message error message
   * @param cause source exception
   */
  public RequestProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
