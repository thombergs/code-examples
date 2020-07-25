package io.reflectoring.client.registration.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.registration.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional
public class StatelessClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(StatelessClient.class);

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
    public void sendAndForget() throws JsonProcessingException {

        CarDto carDto = CarDto.builder()
                .id(UUID.randomUUID())
                .color("white")
                .name("vw")
                .build();
        LOGGER.info("Sending message with routing key {} and id {}", "old.car", carDto.getId());

        UUID correlationId = UUID.randomUUID();

        registrationService.saveCar(carDto, correlationId);

        byte[] body = new ObjectMapper().writeValueAsBytes(carDto);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(replyQueue.getName());
        messageProperties.setCorrelationId(correlationId.toString());
        Message message = MessageBuilder.withBody(body)
                .andProperties(messageProperties).build();

        template.send(directExchange.getName(), "old.car", message);
    }
}
