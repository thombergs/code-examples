package io.reflectoring.client.registration.rpc;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.dto.RegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class StatefulFutureClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(StatefulFutureClient.class);

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directExchange;
    public static final String ROUTING_KEY = "old.car";

    public StatefulFutureClient(AsyncRabbitTemplate asyncRabbitTemplate, DirectExchange directExchange) {
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.directExchange = directExchange;
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public void sendWithFuture() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("black")
                .name("bmw")
                .build();

        ListenableFuture<RegistrationDto> listenableFuture =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(),
                        ROUTING_KEY,
                        carDto,
                        new ParameterizedTypeReference<>() {
                        });
        // non blocking part
        try {
            RegistrationDto registrationDto = listenableFuture.get();
            LOGGER.info("Message received: {}", registrationDto);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Cannot get response.", e);
        }
    }
}
