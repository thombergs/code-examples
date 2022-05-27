package io.reflectoring.dto;

import lombok.*;

@Data
@Builder
@ToString
public class BasicUserDTO {
    private String id;
    private String name;
}
