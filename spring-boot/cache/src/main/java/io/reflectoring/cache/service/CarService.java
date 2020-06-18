package io.reflectoring.cache.service;

import io.reflectoring.cache.dao.Car;
import io.reflectoring.cache.dao.CarRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CacheManager cacheManager;

    public CarService(CarRepository carRepository, CacheManager cacheManager) {
        this.carRepository = carRepository;
        this.cacheManager = cacheManager;
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @CachePut(value = "cars")
    public Car update(Car car) {
        if (carRepository.existsById(car.getId())) {
            return carRepository.save(car);
        }
        // TODO add an exception handle and a dedicated exception
        throw new IllegalArgumentException("A car mus have an id to be updated");
    }

    @Cacheable(value = "cars")
    public Car get(UUID uuid) {
        return carRepository.findById(uuid)
                .orElseThrow(() -> new IllegalStateException("car with id " + uuid + " was not found"));
    }

    @CacheEvict(value = "cars")
    public void delete(UUID uuid) {
        carRepository.deleteById(uuid);
        System.out.println(cacheManager.getClass().getCanonicalName());
//        long cacheHits = ((HazelcastCacheManager) cacheManager).getHazelcastInstance().getMap("cars").getLocalMapStats().getHits();
//        System.out.println(((HazelcastCacheManager) cacheManager).getHazelcastInstance().getMap("cars").getLocalMapStats());
//        long cacheMisses = ((HazelcastCacheManager) cacheManager).getHazelcastInstance().getCacheManager().getCache("cars").getLocalCacheStatistics().getCacheMisses();
//
//        System.out.println("hits:" + cacheHits);
//        System.out.println("misses:" + cacheMisses);
    }

}
