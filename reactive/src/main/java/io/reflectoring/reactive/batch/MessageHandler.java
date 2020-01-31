package io.reflectoring.reactive.batch;

public interface MessageHandler {

  enum Result {
    SUCCESS,
    FAILURE
  }

  Result handleMessage(Message message);

}
