package com.reflectoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.reflectoring.pojo.Employee;

import java.io.File;
import java.io.IOException;

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
}
