package io.reflectoring.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Manager {
    private int id;
    private String name;
    private String dateOfBirth;
}
