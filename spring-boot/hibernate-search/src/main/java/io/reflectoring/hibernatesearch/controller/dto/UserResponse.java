package io.reflectoring.hibernatesearch.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String id;
    private String first;
    private String middle;
    private String last;
    private Integer age;
}
