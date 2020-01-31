package io.reflectoring;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.reflectoring.reactive.batch.MessageSource;
import io.reflectoring.reactive.batch.ReactiveBatchProcessor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ReactiveBatchProcessorTest {

  @Test
  public void allMessagesAreProcessedOnMultipleThreads() {

    int batches = 3;
    int batchSize = 4;
    int threads = 5;

    MessageSource messageSource = new TestMessageSource(batches, batchSize);
    TestMessageHandler messageHandler = new TestMessageHandler();

    ReactiveBatchProcessor processor = new ReactiveBatchProcessor(
        messageSource,
        messageHandler,
        threads
    );

    processor.start();

    await()
        .atMost(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertEquals(batches * batchSize, messageHandler.getProcessedMessages()));

    assertEquals(threads, messageHandler.threadNames().size());
  }

}
