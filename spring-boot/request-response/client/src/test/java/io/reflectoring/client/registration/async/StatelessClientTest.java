package io.reflectoring.client.registration.async;

import io.reflectoring.client.registration.AbstractIntegrationTest;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestPropertySource(properties = "scheduling.enable=false")
class StatelessClientTest extends AbstractIntegrationTest {

    @Autowired
    private StatelessClient statelessClient;

    @Test
    void sendAndForget() {
        // given

        // when
        ThrowableAssert.ThrowingCallable send = () -> statelessClient.sendAndForget();

        // then
        assertThatCode(send).doesNotThrowAnyException();
    }
}