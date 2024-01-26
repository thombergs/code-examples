package com.reflectoring.userdetails.service;

import com.reflectoring.userdetails.mapper.UserMapper;
import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.persistence.User;
import com.reflectoring.userdetails.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserData> getUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.usersToUserData(users);
    }
}
