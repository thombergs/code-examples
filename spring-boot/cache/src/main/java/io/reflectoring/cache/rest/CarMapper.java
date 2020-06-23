package io.reflectoring.cache.rest;

import io.reflectoring.cache.dao.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    CarDto toCarDto(Car car){
        return CarDto.builder()
                .id(car.getId())
                .color(car.getColor())
                .name(car.getName())
                .build();
    }

    Car toCar(CarDto carDto){
        return Car.builder()
                .color(carDto.getColor())
                .id(carDto.getId())
                .name(carDto.getName())
                .build();
    }
}
