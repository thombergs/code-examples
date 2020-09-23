package io.reflectoring.server.rpc;

import io.reflectoring.server.dto.Car;
import io.reflectoring.server.dto.Registration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Consumer {

    @RabbitListener(queues = "#{queue.name}", concurrency = "10")
    public Registration receive(Car car) {
        return Registration.builder()
                .id(car.getId())
                .date(new Date())
                .owner("Ms. Rabbit")
                .signature("Signature of the registration")
                .build();
    }
}
