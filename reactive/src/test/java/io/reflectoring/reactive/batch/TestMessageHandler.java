package io.reflectoring.reactive.batch;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class TestMessageHandler implements MessageHandler {

  private final AtomicInteger processedMessages = new AtomicInteger();

  private final AtomicReference<Set<String>> threadNames = new AtomicReference<>(new HashSet<>());

  private Logger logger = new Logger();

  @Override
  public Result handleMessage(Message message) {
    sleep(500);
    logger.log(String.format("processed message %s", message));
    threadNames.get().add(Thread.currentThread().getName());
    processedMessages.addAndGet(1);
    return Result.SUCCESS;
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public int getProcessedMessages() {
    return processedMessages.get();
  }

  public Set<String> threadNames() {
    return threadNames.get();
  }
}
