package com.reflectoring.begginersguide.services;

import com.reflectoring.begginersguide.domain.User;
import com.reflectoring.begginersguide.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class GetUserService {

    private final UserRepository userRepository;


    public GetUserService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }
}
