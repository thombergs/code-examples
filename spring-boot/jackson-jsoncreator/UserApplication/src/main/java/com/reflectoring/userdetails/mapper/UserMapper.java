package com.reflectoring.userdetails.mapper;

import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.persistence.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {

    public static List<UserData> usersToUserData(List<User> users) {
        return users.stream().map(user -> new UserData(user.getId(),
                user.getFirstName(), user.getLastName(), user.getCity())).collect(Collectors.toList());
    }

}
