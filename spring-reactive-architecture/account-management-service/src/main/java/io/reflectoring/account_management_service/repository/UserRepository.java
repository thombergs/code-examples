package io.reflectoring.account_management_service.repository;

import io.reflectoring.account_management_service.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByCardId(String cardId);
}
