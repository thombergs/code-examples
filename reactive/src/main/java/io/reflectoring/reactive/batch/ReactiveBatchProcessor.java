package io.reflectoring.reactive.batch;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reflectoring.reactive.batch.MessageHandler.Result;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ReactiveBatchProcessor {

  private final static Logger logger = new Logger();

  private final int threads;

  private final MessageHandler messageHandler;

  private final MessageSource messageSource;

  public ReactiveBatchProcessor(
      MessageSource messageSource,
      MessageHandler messageHandler,
      int threads) {
    this.messageSource = messageSource;
    this.threads = threads;
    this.messageHandler = messageHandler;
  }

  public void start() {

    Scheduler scheduler = threadPoolScheduler(threads, 10);

    messageSource.getMessageBatches()
        .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
        .doOnNext(batch -> logger.log(batch.toString()))
        .flatMap(batch -> Flowable.fromIterable(batch.getMessages()))
        .flatMapSingle(m -> Single.defer(() -> Single.just(m)
            .map(messageHandler::handleMessage))
            .subscribeOn(scheduler))
        .subscribeWith(subscriber());
  }

  private Subscriber<MessageHandler.Result> subscriber() {
    return new Subscriber<>() {
      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(threads);
        logger.log("subscribed");
      }

      @Override
      public void onNext(Result message) {
        subscription.request(threads);
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

}
