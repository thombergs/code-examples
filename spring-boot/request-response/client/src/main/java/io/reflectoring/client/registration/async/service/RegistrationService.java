package io.reflectoring.client.registration.async.service;

import io.reflectoring.client.dto.CarDto;
import io.reflectoring.client.dto.RegistrationDto;
import io.reflectoring.client.registration.async.persistance.Car;
import io.reflectoring.client.registration.async.persistance.CarRepository;
import io.reflectoring.client.registration.async.persistance.Registration;
import io.reflectoring.client.registration.async.persistance.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrationService {

    private final CarMapper carMapper;
    private final RegistrationMapper registrationMapper;
    private final CarRepository carRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(CarMapper carMapper, RegistrationMapper registrationMapper, CarRepository carRepository, RegistrationRepository registrationRepository) {
        this.carMapper = carMapper;
        this.registrationMapper = registrationMapper;
        this.carRepository = carRepository;
        this.registrationRepository = registrationRepository;
    }

    public void saveCar(CarDto carDto, UUID correlationId) {
        Car car = carMapper.toCar(carDto);
        car.setCorrelationId(correlationId);
        carRepository.save(car);
    }

    public void saveRegistration(UUID correlationId, RegistrationDto registrationDto){
        Registration registration = registrationMapper.toRegistration(registrationDto);
        Car car = carRepository.findByCorrelationId(correlationId);
        registration.setCar(car);
        registrationRepository.save(registration);
    }
}
