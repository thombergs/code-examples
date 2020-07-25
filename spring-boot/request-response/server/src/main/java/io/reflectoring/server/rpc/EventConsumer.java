package io.reflectoring.server.rpc;

import io.reflectoring.server.dto.Car;
import io.reflectoring.server.dto.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class EventConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);


    @RabbitListener(queues = "#{queue.name}", concurrency = "10")
    public Registration receive(Car car) throws InterruptedException {
        LOGGER.info("Message received {} ", car);
        TimeUnit.SECONDS.sleep(10);
        LOGGER.info("Message proceeded {} ", car);
        return Registration.builder()
                .id(car.getId())
                .date(new Date())
                .owner("Ms. Rabbit")
                .signature("Signature of the registration")
                .build();
    }
}