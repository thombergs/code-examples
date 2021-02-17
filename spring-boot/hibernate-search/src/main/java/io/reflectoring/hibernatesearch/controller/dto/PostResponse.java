package io.reflectoring.hibernatesearch.controller.dto;

import io.reflectoring.hibernatesearch.domain.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private String id;
    private String body;
    private UserResponse user;
    private Tag tag;
    private String imageUrl;
    private String imageDescription;
    private String hashTags;
    private long likeCount;
    private LocalDateTime createdAt;
}
