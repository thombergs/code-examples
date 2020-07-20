package io.reflectoring.server.rpc;

import io.reflectoring.server.dto.Car;
import io.reflectoring.server.dto.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EventConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);


    @RabbitListener(queues = "#{queue.name}")
    public Registration receive(Car car) {
        LOGGER.info("Message received {} ", car);
        return Registration.builder()
                .id(car.getId())
                .date(new Date())
                .owner("Ms. Rabbit")
                .signature("Signature of the registration")
                .build();
    }
}
