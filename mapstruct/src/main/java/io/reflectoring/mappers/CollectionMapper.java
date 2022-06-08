package io.reflectoring.mappers;

import io.reflectoring.dto.EducationDTO;
import io.reflectoring.dto.EmploymentDTO;
import io.reflectoring.model.Education;
import io.reflectoring.model.Employment;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mapper
public interface CollectionMapper {
    CollectionMapper INSTANCE = Mappers.getMapper(CollectionMapper.class);

    Set<String> convert(Set<Long> ids);
    Set<EmploymentDTO> convertEmployment(Set<Employment> employmentSet);
    Set<String> convertStream(Stream<Long> ids);

    @Mapping(source = "degreeName", target = "degree")
    @Mapping(source = "institute", target = "college")
    @Mapping(source = "yearOfPassing", target = "passingYear")
    EducationDTO convert(Education education);
    List<EducationDTO> convert(List<Education> educationList);
    List<EducationDTO> convert(Stream<Education> educationStream);

    @MapMapping(keyNumberFormat = "#L", valueDateFormat = "dd.MM.yyyy")
    Map<String, String> map(Map<Long, Date> dateMap);
}
