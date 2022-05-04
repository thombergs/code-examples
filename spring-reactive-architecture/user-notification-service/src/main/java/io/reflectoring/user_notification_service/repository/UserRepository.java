package io.reflectoring.user_notification_service.repository;

import io.reflectoring.user_notification_service.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByCardId(String cardId);
}
