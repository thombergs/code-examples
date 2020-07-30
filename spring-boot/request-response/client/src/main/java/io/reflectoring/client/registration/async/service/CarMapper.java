package io.reflectoring.client.registration.async.service;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.registration.async.persistance.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    Car toCar(CarDto carDto) {
        return Car.builder()
                .id(carDto.getId())
                .color(carDto.getColor())
                .name(carDto.getName())
                .build();
    }

    CarDto toCarDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .color(car.getColor())
                .name(car.getName())
                .build();
    }
}
