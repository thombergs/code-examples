package io.reflectoring.client.registration.async;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.registration.async.service.RegistrationService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional
public class StatelessClient {

    private final RabbitTemplate template;

    private final DirectExchange directExchange;

    private final Queue replyQueue;


    private final RegistrationService registrationService;

    public StatelessClient(RabbitTemplate template, DirectExchange directExchange, Queue replyQueue, RegistrationService registrationService) {
        this.template = template;
        this.directExchange = directExchange;
        this.replyQueue = replyQueue;
        this.registrationService = registrationService;
    }

    @Scheduled(fixedDelay = 3000)
    public void sendAndForget() {

        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("white")
                .name("vw")
                .build();
        UUID correlationId = UUID.randomUUID();

        registrationService.saveCar(carDto, correlationId);

        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setReplyTo(replyQueue.getName());
            messageProperties.setCorrelationId(correlationId.toString());
            return message;
        };

        template.convertAndSend(directExchange.getName(),
                "old.car",
                carDto,
                messagePostProcessor);
    }
}
