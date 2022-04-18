package io.reflectoring.account_management_service.repository;

import io.reflectoring.account_management_service.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
