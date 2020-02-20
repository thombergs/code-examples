package io.reflectoring.reactive.batch;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class ReactiveBatchProcessorTest {

  @Test
  void allMessagesAreProcessedOnMultipleThreads() {

    int batches = 10;
    int batchSize = 3;
    int threads = 2;
    int threadPoolQueueSize = 10;

    MessageSource messageSource = new TestMessageSource(batches, batchSize);
    TestMessageHandler messageHandler = new TestMessageHandler();

    ReactiveBatchProcessor processor = new ReactiveBatchProcessor(
        messageSource,
        messageHandler,
        threads,
        threadPoolQueueSize);

    processor.start();

    await()
        .atMost(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertEquals(batches * batchSize, messageHandler.getProcessedMessages()));

    assertEquals(threads, messageHandler.threadNames().size());
  }

}
