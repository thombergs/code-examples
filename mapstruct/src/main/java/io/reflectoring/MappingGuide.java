package io.reflectoring;

import io.reflectoring.dto.BasicUserDTO;
import io.reflectoring.dto.EducationDTO;
import io.reflectoring.dto.PersonDTO;
import io.reflectoring.mappers.CollectionMapper;
import io.reflectoring.mappers.UserMapper;
import io.reflectoring.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class MappingGuide {
    public static void main(String[] args) {
        log.info("MapStruct Mapping conversion started !!");

        Manager manager = Manager
                .builder()
                .id(2)
                .name("Adam Smith")
                .build();
        BasicUser user = BasicUser
                .builder()
                .id(1)
                .name("John Doe")
                .managerList(Collections.singletonList(manager))
                .build();
        log.info("User details: {}", user);

        BasicUserDTO dto = UserMapper.INSTANCE.convert(user);
        log.info("UserDTO details: {}", dto);

        BasicUser anotherUser = UserMapper.INSTANCE.convert(dto);
        log.info("User details: {}", anotherUser);

        Education education = Education
                .builder()
                .degreeName("M.S.")
                .institute("Carnegie Mellon University")
                .yearOfPassing(2021)
                .build();
        Address address = Address
                .builder()
                .houseNo("25A")
                .landmark("Decker's lane")
                .city("New York")
                .country("USA")
                .zipcode("10001")
                .build();
        Employment employment = Employment
                .builder()
                .designation(DesignationCode.CEO)
                .salary(300000L)
                .build();
        PersonDTO personDTO = UserMapper.INSTANCE.convert(user, education, address, employment);
        UserMapper.INSTANCE.updateExisting(user, education, address, employment, personDTO);
        log.info("PersonDTO details: {}", personDTO);

        Set<Long> ids = new HashSet<>(Arrays.asList(100000L, 100001L, 100002L));
        Set<String> set = CollectionMapper.INSTANCE.convert(ids);
        Set<String> streamSet = CollectionMapper.INSTANCE.convertStream(ids.parallelStream());
        log.info("Set of String: {}", set);
        log.info("Set of String from Streams: {}", streamSet);

        List<EducationDTO> educationDTO = CollectionMapper.INSTANCE.convert(Collections.singletonList(education));
        List<EducationDTO> educationDTOFromStream = CollectionMapper.INSTANCE.convert(Stream.of(education));
        log.info("List of Education: {}", educationDTO);
        log.info("List of Education from Stream: {}", educationDTOFromStream);

        Map<Long, Date> dateMap = new HashMap<>();
        dateMap.put(100000L, new Date());
        dateMap.put(100001L, new Date());
        Map<String, String> map = CollectionMapper.INSTANCE.map(dateMap);
        log.info("Map of dates: {}", map);

        log.info("MapStruct Mapping conversion completed !!");
    }
}
