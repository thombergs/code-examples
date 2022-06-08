package io.reflectoring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class PersonDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String educationalQualification;
    private String residentialCity;
    private String residentialCountry;
    private DesignationConstant designation;
    private String salary;
    private EducationDTO education;
    private List<ManagerDTO> managerList;
}
