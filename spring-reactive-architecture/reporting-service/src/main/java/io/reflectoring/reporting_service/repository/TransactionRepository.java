package io.reflectoring.reporting_service.repository;

import io.reflectoring.reporting_service.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
