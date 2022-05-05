package io.reflectoring.user_notification_service.repository;

import io.reflectoring.user_notification_service.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
