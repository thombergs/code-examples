package io.reflectoring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EducationDTO {
    private String degree;
    private String college;
    private Integer passingYear;
}
