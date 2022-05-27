package io.reflectoring.mappers;

import io.reflectoring.decorators.UserMapperDecorator;
import io.reflectoring.dto.BasicUserDTO;
import io.reflectoring.dto.DegreeStreamPrefix;
import io.reflectoring.dto.DesignationConstant;
import io.reflectoring.dto.PersonDTO;
import io.reflectoring.exception.ValidationException;
import io.reflectoring.model.*;
import io.reflectoring.util.Validator;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = {CollectionMapper.class, ManagerMapper.class, Validator.class},
        imports = UUID.class )
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.id", target = "id", defaultExpression = "java( UUID.randomUUID().toString() )")
    BasicUserDTO convert(BasicUser user) throws ValidationException;
    default PersonDTO convertCustom(BasicUser user) {
        return PersonDTO
                .builder()
                .id(String.valueOf(user.getId()))
                .firstName(user.getName().substring(0, user.getName().indexOf(" ")))
                .lastName(user.getName().substring(user.getName().indexOf(" ") + 1))
                .build();
    }

    @InheritInverseConfiguration
    BasicUser convert(BasicUserDTO userDTO) throws ValidationException;

    @ValueMappings({
            @ValueMapping(source = "CEO", target = "CHIEF_EXECUTIVE_OFFICER"),
            @ValueMapping(source = "CTO", target = "CHIEF_TECHNICAL_OFFICER"),
            @ValueMapping(source = "VP", target = "VICE_PRESIDENT"),
            @ValueMapping(source = "SM", target = "SENIOR_MANAGER"),
            @ValueMapping(source = "M", target = "MANAGER"),
            @ValueMapping(source = "ARCH", target = "ARCHITECT"),
            @ValueMapping(source = "SSE", target = "SENIOR_SOFTWARE_ENGINEER"),
            @ValueMapping(source = "SE", target = "SOFTWARE_ENGINEER"),
            @ValueMapping(source = "INT", target = "INTERN"),
            @ValueMapping(source = MappingConstants.ANY_REMAINING, target = "OTHERS"),
            @ValueMapping(source = MappingConstants.NULL, target = "OTHERS")
    })
    DesignationConstant convertDesignation(DesignationCode code);

    @EnumMapping(nameTransformationStrategy = "prefix", configuration = "MSC_")
    DegreeStreamPrefix convert(DegreeStream degreeStream);

    @EnumMapping(nameTransformationStrategy = "stripPrefix", configuration = "MSC_")
    DegreeStream convert(DegreeStreamPrefix degreeStreamPrefix);

    @BeforeMapping
    default void validateManagers(BasicUser user, Education education, Address address, Employment employment) {
        if (Objects.isNull(user.getManagerList())) {
            user.setManagerList(new ArrayList<>());
        }
    }

    @Mapping(source = "user.id", target = "id", defaultExpression = "java( UUID.randomUUID().toString() )")
    @Mapping(source = "education.degreeName", target = "education.degree")
    @Mapping(source = "education.institute", target = "education.college")
    @Mapping(source = "education.yearOfPassing", target = "education.passingYear", defaultValue = "2001")
    @Mapping(source = "employment", target = ".")
    PersonDTO convert(BasicUser user, Education education, Address address, Employment employment);

    @Mapping(source = "education.degreeName", target = "educationalQualification")
    @Mapping(source = "address.city", target = "residentialCity")
    @Mapping(target = "residentialCountry", constant = "US")
    @Mapping(source = "employment.salary", target = "salary", numberFormat = "$#.00")
    void updateExisting(BasicUser user, Education education, Address address, Employment employment, @MappingTarget PersonDTO personDTO);

    @AfterMapping
    default void updateResult(BasicUser user, Education education, Address address, Employment employment, @MappingTarget PersonDTO personDTO) {
        personDTO.setFirstName(personDTO.getFirstName().toUpperCase());
        personDTO.setLastName(personDTO.getLastName().toUpperCase());
    }
}
