package io.reflectoring.client.registration.rpc;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.dto.RegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Component
public class StatefulCallbackClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(StatefulFutureClient.class);

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directExchange;
    public static final String ROUTING_KEY = "old.car";

    public StatefulCallbackClient(AsyncRabbitTemplate asyncRabbitTemplate, DirectExchange directExchange) {
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.directExchange = directExchange;
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public void sendAsynchronouslyWithCallback() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("black")
                .name("bmw")
                .build();
        RabbitConverterFuture<RegistrationDto> rabbitConverterFuture =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(),
                        ROUTING_KEY,
                        carDto,
                        new ParameterizedTypeReference<>() {});
        rabbitConverterFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("Cannot get response for: {}", carDto.getId(), ex);
            }

            @Override
            public void onSuccess(RegistrationDto registrationDto) {
                LOGGER.info("Registration received {}", registrationDto);
            }
        });
    }
}
