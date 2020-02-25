package io.reflectoring.reactive.batch;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReactiveBatchProcessorV1 {

  private final static Logger logger = new Logger();

  private final int threads;

  private final int threadPoolQueueSize;

  private final MessageHandler messageHandler;

  private final MessageSource messageSource;

  public ReactiveBatchProcessorV1(
      MessageSource messageSource,
      MessageHandler messageHandler,
      int threads,
      int threadPoolQueueSize) {
    this.messageSource = messageSource;
    this.threads = threads;
    this.messageHandler = messageHandler;
    this.threadPoolQueueSize = threadPoolQueueSize;
  }

  public void start() {
    // WARNING: this code doesn't work as expected
    messageSource.getMessageBatches()
        .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
        .doOnNext(batch -> logger.log(batch.toString()))
        .flatMap(batch -> Flowable.fromIterable(batch.getMessages()))
        .flatMapSingle(m -> Single.just(messageHandler.handleMessage(m))
            .subscribeOn(threadPoolScheduler(threads, threadPoolQueueSize)))
        .subscribeWith(new SimpleSubscriber<>(threads, 1));
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

}
