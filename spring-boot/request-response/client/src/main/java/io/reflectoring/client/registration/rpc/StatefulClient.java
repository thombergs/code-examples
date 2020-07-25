package io.reflectoring.client.registration.rpc;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.dto.RegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class StatefulClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(StatefulClient.class);

    private final RabbitTemplate template;

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directExchange;
    public static final String ROUTING_KEY = "old.car";

    public StatefulClient(DirectExchange directExchange, RabbitTemplate template,
                          AsyncRabbitTemplate asyncRabbitTemplate) {
        this.directExchange = directExchange;
        this.template = template;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void send() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("white")
                .name("vw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", ROUTING_KEY, carDto.getId());

        ParameterizedTypeReference<RegistrationDto> responseType
                = new ParameterizedTypeReference<>() {
        };
        RegistrationDto registrationDto = template.convertSendAndReceiveAsType(
                directExchange.getName(), ROUTING_KEY, carDto, responseType);
        LOGGER.info("Message received: {}", registrationDto);
    }




    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public void sendAsynchronously() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("black")
                .name("bmw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", ROUTING_KEY, carDto.getId());

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
            LOGGER.info("Asynchronous message received: {}", registrationDto);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Cannot get response.", e);
        }
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public void sendAsynchronouslyWithCallback() {
        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("black")
                .name("bmw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", ROUTING_KEY, carDto.getId());

        ParameterizedTypeReference<RegistrationDto> responseType
                = new ParameterizedTypeReference<>() {
        };
        RabbitConverterFuture<RegistrationDto> rabbitConverterFuture =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(), ROUTING_KEY, carDto, responseType);

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
        LOGGER.info("Message {} sent", carDto);
    }
}
