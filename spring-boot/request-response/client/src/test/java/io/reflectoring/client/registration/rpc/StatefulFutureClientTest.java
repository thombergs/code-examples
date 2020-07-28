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
class StatefulFutureClientTest extends AbstractIntegrationTest {

    @Autowired
    private StatefulFutureClient statefulFutureClient;

    @Test
    void sendAsynchronously() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send =
                () -> statefulFutureClient.sendWithFuture();

        // then
        assertThatCode(send)
                .doesNotThrowAnyException();
    }
}