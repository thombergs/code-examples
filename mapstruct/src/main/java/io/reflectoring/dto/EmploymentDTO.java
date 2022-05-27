package io.reflectoring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EmploymentDTO {
    private String designation;
    private long salary;
}
