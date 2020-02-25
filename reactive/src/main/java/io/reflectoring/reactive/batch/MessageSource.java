package io.reflectoring.reactive.batch;

import io.reactivex.rxjava3.core.Flowable;

interface MessageSource {

  Flowable<MessageBatch> getMessageBatches();

}
