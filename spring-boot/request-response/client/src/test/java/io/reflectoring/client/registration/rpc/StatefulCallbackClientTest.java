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
class StatefulCallbackClientTest extends AbstractIntegrationTest {

    @Autowired
    private StatefulCallbackClient statefulCallbackClient;

    @Test
    void sendAsynchronouslyWithCallback() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send = () -> statefulCallbackClient.sendAsynchronouslyWithCallback();

        // then
        assertThatCode(send).doesNotThrowAnyException();
    }
}