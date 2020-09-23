package io.reflectoring.client.registration.rpc;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.dto.RegistrationDto;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StatefulBlockingClient {

    private final RabbitTemplate template;

    private final DirectExchange directExchange;
    public static final String ROUTING_KEY = "old.car";

    public StatefulBlockingClient(DirectExchange directExchange, RabbitTemplate template) {
        this.directExchange = directExchange;
        this.template = template;
    }

    @Scheduled(fixedDelay = 3000)
    public void send() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("white")
                .name("vw")
                .build();
        RegistrationDto registrationDto = template.convertSendAndReceiveAsType(
                directExchange.getName(),
                ROUTING_KEY,
                carDto,
                new ParameterizedTypeReference<>() {
                });
    }
}
