package io.reflectoring.springcloudredis.service;

import io.reflectoring.springcloudredis.entity.User;
import io.reflectoring.springcloudredis.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "product-cache")
public class UserService {

    private final UserRepository repository;

    @Cacheable
    public User getUser(String id){
        return repository.findById(id).orElseThrow(()->
                new RuntimeException("No such user found with id"));
    }
}
