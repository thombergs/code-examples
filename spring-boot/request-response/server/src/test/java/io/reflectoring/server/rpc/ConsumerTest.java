package io.reflectoring.server.rpc;

import io.reflectoring.server.dto.Car;
import io.reflectoring.server.dto.Registration;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConsumerTest extends AbstractIntegrationTest{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange directExchange;

    @Test
    void send() {
        // given
        UUID id = UUID.randomUUID();
        Car car = Car.builder()
                .id(id)
                .name("vw")
                .color("white")
                .build();

        // when
        ParameterizedTypeReference<Registration> responseType =
                new ParameterizedTypeReference<>() {};
        Registration registration = rabbitTemplate.convertSendAndReceiveAsType(directExchange.getName(), "old.car", car, responseType);

        // then
        assertThat(registration).isNotNull();
        assertThat(registration.getId()).isEqualTo(id);
    }
}