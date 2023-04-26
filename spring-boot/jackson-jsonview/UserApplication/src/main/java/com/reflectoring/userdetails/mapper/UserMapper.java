package com.reflectoring.userdetails.mapper;

import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.persistence.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserData userToUserData(User user);

    List<UserData> usersToUserData(List<User> users);

    User userDataToUser(UserData userData);

}
