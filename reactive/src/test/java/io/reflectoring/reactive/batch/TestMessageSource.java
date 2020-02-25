package io.reflectoring.reactive.batch;

import io.reactivex.rxjava3.core.Flowable;
import io.reflectoring.reactive.batch.Message;
import io.reflectoring.reactive.batch.MessageBatch;
import io.reflectoring.reactive.batch.MessageSource;
import java.util.ArrayList;
import java.util.List;

class TestMessageSource implements MessageSource {

  private final int batches;

  private final int batchSize;

  /**
   * Constructor.
   *
   * @param batches   the number of message batches to produce.
   * @param batchSize the number of messages per batch.
   */
  TestMessageSource(int batches, int batchSize) {
    this.batches = batches;
    this.batchSize = batchSize;
  }

  /**
   * Generates a limited number of {@link MessageBatch}es.
   */
  public Flowable<MessageBatch> getMessageBatches() {
    return Flowable.range(1, batches)
        .map(this::messageBatch);
  }

  private MessageBatch messageBatch(int batchNumber) {
    List<Message> messages = new ArrayList<>();
    for (int i = 1; i <= batchSize; i++) {
      messages.add(new Message(String.format("%d-%d", batchNumber, i)));
    }
    return new MessageBatch(messages);
  }

}
