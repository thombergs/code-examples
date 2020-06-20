package io.reflectoring.cache.rest;

import io.reflectoring.cache.dao.Car;
import io.reflectoring.cache.service.CarService;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cars")
@Transactional
public class CarResource {

    private final CarService carService;
    private final CarMapper carMapper;
    private final CacheManager cacheManager;

    public CarResource(CarService carService, CarMapper carMapper, CacheManager cacheManager) {
        this.carService = carService;
        this.carMapper = carMapper;
        this.cacheManager = cacheManager;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    private CarDto createCar(@RequestBody CarDto carDto) {
        Car car = carMapper.toCar(carDto);
        return carMapper.toCarDto(carService.saveCar(car));
    }

    @PutMapping
    private CarDto updateCar(@RequestBody CarDto carDto) {
        Car car = carMapper.toCar(carDto);
        return carMapper.toCarDto(carService.update(car));
    }

    @GetMapping(value = "/{uuid}")
    private CarDto get(@PathVariable UUID uuid){
        return carMapper.toCarDto(carService.get(uuid));
    }

    @DeleteMapping(value = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable  UUID uuid){
      carService.delete(uuid);
    }


}
