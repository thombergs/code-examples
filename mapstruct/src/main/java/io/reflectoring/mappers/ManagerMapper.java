package io.reflectoring.mappers;

import io.reflectoring.dto.ManagerDTO;
import io.reflectoring.model.Manager;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ManagerMapper {
    ManagerMapper INSTANCE = Mappers.getMapper(ManagerMapper.class);

    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "dd.MM.yyyy")
    ManagerDTO convert(Manager manager);

    @InheritConfiguration
    void updateExisting(Manager manager, @MappingTarget ManagerDTO managerDTO);
}
