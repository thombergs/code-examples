package io.reflectoring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Builder
@ToString
public class ManagerDTO {
    private int id;
    private String name;
    private Date dateOfBirth;
}
