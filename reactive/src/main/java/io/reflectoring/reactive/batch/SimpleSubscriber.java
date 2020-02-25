package io.reflectoring.reactive.batch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class SimpleSubscriber<T> implements Subscriber<T> {

  private final Logger logger = new Logger();

  private final int initialFetchCount;
  private final int onNextFetchCount;

  private Subscription subscription;

  SimpleSubscriber(int initialFetchCount, int onNextFetchCount) {
    this.initialFetchCount = initialFetchCount;
    this.onNextFetchCount = onNextFetchCount;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    this.subscription = subscription;
    subscription.request(initialFetchCount);
    logger.log("subscribed");
  }

  @Override
  public void onNext(T message) {
    subscription.request(onNextFetchCount);
  }

  @Override
  public void onError(Throwable t) {
    logger.log("error");
  }

  @Override
  public void onComplete() {
    logger.log("completed");
  }

}
