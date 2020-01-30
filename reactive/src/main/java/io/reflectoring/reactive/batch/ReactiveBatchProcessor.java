package io.reflectoring.reactive.batch;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reflectoring.reactive.batch.MessageHandler.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ReactiveBatchProcessor {

  private final static Logger logger = new Logger();

  private final int MESSAGE_BATCHES = 10;

  private final int BATCH_SIZE = 3;

  private final int THREADS = 4;

  private final MessageHandler messageHandler = new MessageHandler();

  public void start() {
    retrieveMessageBatches()
        .doOnNext(batch -> logger.log(batch.toString()))
        .flatMap(batch -> Flowable.fromIterable(batch.getMessages()))
        .flatMapSingle(message -> messageHandler.handleMessage(message)
            .subscribeOn(threadPoolScheduler(THREADS, 10)))
        .subscribeWith(subscriber());
  }

  private Subscriber<MessageHandler.Result> subscriber() {
    return new Subscriber<>() {
      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(THREADS);
        logger.log("subscribed");
      }

      @Override
      public void onNext(Result message) {
        subscription.request(THREADS);
      }

      @Override
      public void onError(Throwable t) {
        logger.log("error");
      }

      @Override
      public void onComplete() {
        logger.log("completed");
      }
    };
  }

  private Scheduler threadPoolScheduler(int poolSize, int queueSize) {
    return Schedulers.from(new ThreadPoolExecutor(
        poolSize,
        poolSize,
        0L,
        TimeUnit.SECONDS,
        new LinkedBlockingDeque<>(queueSize)
    ));
  }

  public boolean allMessagesProcessed() {
    return this.messageHandler.getProcessedMessages().get() == MESSAGE_BATCHES * BATCH_SIZE;
  }

  private Flowable<MessageBatch> retrieveMessageBatches() {
    return Flowable.range(1, MESSAGE_BATCHES)
        .map(this::messageBatch);
  }

  private MessageBatch messageBatch(int batchNumber) {
    List<Message> messages = new ArrayList<>();
    for (int i = 1; i <= BATCH_SIZE; i++) {
      messages.add(new Message(String.format("%d-%d", batchNumber, i)));
    }
    return new MessageBatch(messages);
  }


}
