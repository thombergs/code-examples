package io.reflectoring.client.registration.async.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    Car findByCorrelationId(UUID correlationId);
}
