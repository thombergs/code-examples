package io.reflectoring.reactive.batch;

interface MessageHandler {

  enum Result {
    SUCCESS,
    FAILURE
  }

  Result handleMessage(Message message);

}
