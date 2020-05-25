package io.reflectoring.testcontainers.repositories;

import io.reflectoring.testcontainers.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
}
