package com.reflectoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.pojo.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

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

    @Test
    void fileToPojoWithUnknownProperties() throws IOException {
        File file = new File("src/test/resources/employeeWithUnknownProperties.json");

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Employee employee = objectMapper.readValue(file, Employee.class);

        assertThat(employee.getFirstName()).isEqualTo("Homer");
        assertThat(employee.getLastName()).isEqualTo("Simpson");
        assertThat(employee.getAge()).isEqualTo(44);
    }

    @Test
    void orderToJson() throws JsonProcessingException {
        Order order = new Order(1, LocalDate.of(1900,2,1));

        String json = objectMapper.writeValueAsString(order);

        System.out.println(json);
    }

    @Test
    void orderToJsonWithDate() throws JsonProcessingException {
        Order order = new Order(1, LocalDate.of(2023, 1, 1));

        String json = objectMapper.writeValueAsString(order);

        System.out.println(json);
    }
    @Test
    void fileToOrder() throws IOException {
        File file = new File("src/test/resources/order.json");

        Order order = objectMapper.readValue(file, Order.class);

        assertThat(order.getDate().getYear()).isEqualTo(2000);
        assertThat(order.getDate().getMonthValue()).isEqualTo(4);
        assertThat(order.getDate().getDayOfMonth()).isEqualTo(30);
    }
    @Test
    void fileToCar() throws IOException {
        File file = new File("src/test/resources/car.json");

        Car car = objectMapper.readValue(file, Car.class);

        assertThat(car.getBrand()).isEqualTo("BMW");
    }

    @Test
    void fileToUnrecognizedCar() throws IOException {
        File file = new File("src/test/resources/carUnrecognized.json");

        Car car = objectMapper.readValue(file, Car.class);

        assertThat(car.getUnrecognizedFields()).containsKey("productionYear");
    }

    @Test
    void catToJson() throws JsonProcessingException {
        Cat cat = new Cat("Monica");

        String json = objectMapper.writeValueAsString(cat);

        System.out.println(json);

    }

    @Test
    void catToJsonWithMap() throws JsonProcessingException {
        Cat cat = new Cat("Monica");

        String json = objectMapper.writeValueAsString(cat);

        System.out.println(json);
    }

    @Test
    void dogToJson() throws JsonProcessingException {
        Dog dog = new Dog("Max", 3);

        String json = objectMapper.writeValueAsString(dog);

        System.out.println(json);
    }
    @Test
    void fileToDog() throws IOException {
        File file = new File("src/test/resources/dog.json");

        Dog dog = objectMapper.readValue(file, Dog.class);

        assertThat(dog.getName()).isEqualTo("bobby");
        assertThat(dog.getAge()).isNull();
    }
}
