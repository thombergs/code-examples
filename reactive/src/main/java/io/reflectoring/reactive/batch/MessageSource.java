package io.reflectoring.reactive.batch;

import io.reactivex.Flowable;

public interface MessageSource {

  Flowable<MessageBatch> getMessageBatches();

}
