package io.reflectoring.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
public class BasicUser {
    private int id;
    private String name;
    private List<Manager> managerList;
}
