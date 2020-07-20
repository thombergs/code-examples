package io.reflectoring.client.rpc;

import io.reflectoring.client.dto.Car;
import io.reflectoring.client.dto.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class EventPublisher {

    public static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate template;

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directExchange;

    public EventPublisher(DirectExchange directExchange, RabbitTemplate template,
                          AsyncRabbitTemplate asyncRabbitTemplate) {
        this.directExchange = directExchange;
        this.template = template;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void send() {
        String key = "vw";
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .color("white")
                .name("vw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", key, car.getId());

        ParameterizedTypeReference<Registration> responseType
                = new ParameterizedTypeReference<>() {
        };
        Registration registration = template.convertSendAndReceiveAsType(
                directExchange.getName(), key, car, responseType);
        LOGGER.info("Message received: {}", registration);
    }


    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public void sendAsynchronously() {
        String key = "vw";
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .color("black")
                .name("bmw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", key, car.getId());

        ParameterizedTypeReference<Registration> responseType
                = new ParameterizedTypeReference<>() {
        };
        AsyncRabbitTemplate.RabbitConverterFuture<Registration> future =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(), key, car, responseType);
        try {
            Registration registration = future.get();
            LOGGER.info("Asynchronous message received: {}", registration);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Cannot get response.", e);
        }
    }
}
