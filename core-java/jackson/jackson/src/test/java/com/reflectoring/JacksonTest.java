package com.reflectoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.reflectoring.pojo.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void jsonStringToPojo() throws JsonProcessingException {
        String employeeJson = "{\n" +
                " \"firstName\" : \"Jalil\",\n" +
                "  \"lastName\" : \"Jarjanazy\",\n" +
                "  \"age\" : 30\n" +
                "}";

        Employee employee = objectMapper.readValue(employeeJson, Employee.class);

        assertThat(employee.getFirstName()).isEqualTo("Jalil");
    }

    @Test
    void pojoToJsonString() throws JsonProcessingException {
        Employee employee = new Employee("Mark", "James", 20);

        String json = objectMapper.writeValueAsString(employee);

        assertThat(json).isEqualTo("{\"firstName\":\"Mark\",\"lastName\":\"James\",\"age\":20}");
    }

    @Test
    void jsonFileToPojo() throws IOException {
        File file = new File("src/test/resources/employee.json");

        Employee employee = objectMapper.readValue(file, Employee.class);

        assertThat(employee.getAge()).isEqualTo(44);
        assertThat(employee.getLastName()).isEqualTo("Simpson");
        assertThat(employee.getFirstName()).isEqualTo("Homer");
    }

    @Test
    void byteArrayToPojo() throws IOException {
        String employeeJson = "{\n" +
                " \"firstName\" : \"Jalil\",\n" +
                "  \"lastName\" : \"Jarjanazy\",\n" +
                "  \"age\" : 30\n" +
                "}";

        Employee employee = objectMapper.readValue(employeeJson.getBytes(), Employee.class);

        assertThat(employee.getFirstName()).isEqualTo("Jalil");
    }

    @Test
    void fileToListOfPojos() throws IOException {
        File file = new File("src/test/resources/employeeList.json");
        List<Employee> employeeList = objectMapper.readValue(file, new TypeReference<>(){});

        assertThat(employeeList).hasSize(2);
        assertThat(employeeList.get(0).getAge()).isEqualTo(33);
        assertThat(employeeList.get(0).getLastName()).isEqualTo("Simpson");
        assertThat(employeeList.get(0).getFirstName()).isEqualTo("Marge");
    }

    @Test
    void fileToMap() throws IOException {
        File file = new File("src/test/resources/employee.json");
        Map<String, Object> employee = objectMapper.readValue(file, new TypeReference<>(){});

        assertThat(employee.keySet()).containsExactly("firstName", "lastName", "age");

        assertThat(employee.get("firstName")).isEqualTo("Homer");
        assertThat(employee.get("lastName")).isEqualTo("Simpson");
        assertThat(employee.get("age")).isEqualTo(44);
    }
}
