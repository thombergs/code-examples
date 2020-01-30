package io.reflectoring.reactive.batch;

import io.reactivex.Single;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandler {

    private final AtomicInteger processedMessages = new AtomicInteger();

    private Logger logger = new Logger();

    enum Result {
        SUCCESS,
        FAILURE
    }

    public Result handleMessage(Message message) {
        logger.log(String.format("handling message %s", message));
        sleep(500);
        this.processedMessages.getAndAdd(1);
        return Result.SUCCESS;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getProcessedMessages() {
        return processedMessages.get();
    }
}
