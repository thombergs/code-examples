package io.reflectoring.hibernatesearch.controller.mapper;

import io.reflectoring.hibernatesearch.controller.dto.UserResponse;
import io.reflectoring.hibernatesearch.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
