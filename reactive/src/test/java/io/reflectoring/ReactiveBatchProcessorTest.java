package io.reflectoring;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.reflectoring.reactive.batch.ReactiveBatchProcessor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ReactiveBatchProcessorTest {

  @Test
  public void test() {
    ReactiveBatchProcessor processor = new ReactiveBatchProcessor();

    processor.start();

    await()
        .atMost(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertTrue(processor.allMessagesProcessed()));

  }

}
