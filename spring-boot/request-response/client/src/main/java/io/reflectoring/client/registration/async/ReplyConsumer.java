package io.reflectoring.client.registration.async;


import io.reflectoring.client.dto.RegistrationDto;
import io.reflectoring.client.registration.async.service.RegistrationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
@Transactional
public class ReplyConsumer {

    private final RegistrationService registrationService;

    public ReplyConsumer(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RabbitListener(queues = "#{response.name}", concurrency = "10")
    public void receive(RegistrationDto registrationDto, Message message){
        String correlationId = message.getMessageProperties().getCorrelationId();
        registrationService.saveRegistration(UUID.fromString(correlationId), registrationDto);
    }
}
