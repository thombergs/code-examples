package io.reflectoring.hibernatesearch.controller.mapper;

import io.reflectoring.hibernatesearch.controller.dto.PostResponse;
import io.reflectoring.hibernatesearch.domain.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PostMapper {
    PostResponse toResponse(Post post);
}