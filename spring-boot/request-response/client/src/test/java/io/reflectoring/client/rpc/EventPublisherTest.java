package io.reflectoring.client.rpc;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.RabbitMQContainer;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestPropertySource(properties = "scheduling.enable=false")
class EventPublisherTest {

    private final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer();

    @Autowired
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        rabbitMQContainer.start();
    }

    @Test
    void sendMessageSynchronously() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send = () -> eventPublisher.send();

        // then
        assertThatCode(send).doesNotThrowAnyException();
    }

    @Test
    void sendMessageAsynchronously() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send = () -> eventPublisher.sendAsynchronously();

        // then
        assertThatCode(send).doesNotThrowAnyException();
    }
}