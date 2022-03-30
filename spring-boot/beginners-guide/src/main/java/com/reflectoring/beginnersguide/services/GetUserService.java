package com.reflectoring.beginnersguide.services;

import com.reflectoring.beginnersguide.domain.User;
import com.reflectoring.beginnersguide.repository.UserRepository;
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
