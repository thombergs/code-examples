package io.reflectoring.reactive.batch;

import io.reactivex.Single;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandler {

  private final AtomicInteger processedMessages = new AtomicInteger();

  private Logger logger = new Logger();

  enum Result {
    SUCCESS,
    FAILURE
  }

  public Single<Result> handleMessage(Message message){
    sleep(500);
    logger.log(String.format("processed message %s", message));
    return Single.just(Result.SUCCESS);
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public AtomicInteger getProcessedMessages() {
    return processedMessages;
  }
}
