package io.reflectoring.client.registration.rpc;

import io.reflectoring.client.registration.AbstractIntegrationTest;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestPropertySource(properties = "scheduling.enable=false")
class StatefulBlockingClientTest extends AbstractIntegrationTest {

    @Autowired
    private StatefulBlockingClient statefulBlockingClient;

    @Test
    void sendMessageSynchronously() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send = () -> statefulBlockingClient.send();

        // then
        assertThatCode(send).doesNotThrowAnyException();
    }
}