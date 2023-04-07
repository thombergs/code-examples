package com.reflectoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.persistence.Views;
import com.reflectoring.util.MockedUsersUtility;
import org.hamcrest.beans.PropertyUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JsonViewTest {

    @Test
    public void serializeUserSummaryViewTest() throws JsonProcessingException {
        final UserData mockedUser = MockedUsersUtility.getMockedUserData();

        // DEFAULT_VIEW_INCLUSION is disabled - Fields not annotated with @JsonView are not present
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        final String serializedValue = objectMapper
                .writerWithView(Views.UserSummary.class)
                .writeValueAsString(mockedUser);

        final List<String> expectedFields = Arrays.asList("createdBy", "createdDate", "updatedBy", "updatedDate",
                "additionalData", "loginId", "loginPassword", "ssnNumber");
        expectedFields.stream().forEach(field -> {
            assertFalse(serializedValue.contains(field));
        });

        // DEFAULT_VIEW_INCLUSION is enabled - Fields not annotated with @JsonView are present
        final ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        final String serializedValue1 = objectMapper1
                .writerWithView(Views.UserSummary.class)
                .writeValueAsString(mockedUser);
        System.out.println(serializedValue1);

        assertTrue(serializedValue1.contains("additionalData"));
    }

    @Test
    public void serializeUserDetailedSummaryViewTest() throws JsonProcessingException {
        final UserData mockedUser = MockedUsersUtility.getMockedUserData();

        // DEFAULT_VIEW_INCLUSION is disabled - Fields not annotated with @JsonView are not present
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        final String serializedValue = objectMapper
                .writerWithView(Views.UserDetailedSummary.class)
                .writeValueAsString(mockedUser);

        final List<String> expectedFields = Arrays.asList("createdBy", "createdDate", "updatedBy", "updatedDate");
        expectedFields.stream().forEach(field -> {
            assertTrue(serializedValue.contains(field));
        });

        final List<String> moreFields = Arrays.asList("loginId", "loginPassword", "ssnNumber");
        moreFields.stream().forEach(field -> {
            assertFalse(serializedValue.contains(field));
        });

        // DEFAULT_VIEW_INCLUSION is enabled - Fields not annotated with @JsonView are present
        final ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());
        objectMapper1.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        final String serializedValue1 = objectMapper1
                .writerWithView(Views.UserDetailedSummary.class)
                .writeValueAsString(mockedUser);
        System.out.println(serializedValue1);

        assertTrue(serializedValue1.contains("additionalData"));
    }

    @Test
    public void deserializeUserSummaryViewTest() throws JsonProcessingException {
        // DEFAULT_VIEW_INCLUSION is disabled - Fields not annotated with @JsonView are not present
        // Deserializes only the fields present in the view
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        final UserData deserializedValue = objectMapper
                .readerWithView(Views.UserSummary.class)
                .forType(UserData.class)
                .readValue(MockedUsersUtility.userDataObjectAsString());

        System.out.println("Deserialize with DEFAULT_VIEW_INCLUSION as false :" + deserializedValue);

        assertTrue(Objects.isNull(deserializedValue.getCreatedBy()));
        assertTrue(Objects.isNull(deserializedValue.getCreatedDate()));
        assertTrue(Objects.isNull(deserializedValue.getUpdatedBy()));
        assertTrue(Objects.isNull(deserializedValue.getUpdatedDate()));
        assertTrue(Objects.isNull(deserializedValue.getAdditionalData()));

    }

    @Test
    public void deserializeUserDetailedSummaryViewTest() throws JsonProcessingException {
        // DEFAULT_VIEW_INCLUSION is disabled - Fields not annotated with @JsonView are not present
        // Deserializes only the fields present in the view
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        final UserData deserializedValue = objectMapper
                .readerWithView(Views.UserDetailedSummary.class)
                .forType(UserData.class)
                .readValue(MockedUsersUtility.userDataObjectAsString());

        assertFalse(Objects.isNull(deserializedValue.getCreatedBy()));
        assertFalse(Objects.isNull(deserializedValue.getCreatedDate()));
        assertFalse(Objects.isNull(deserializedValue.getUpdatedBy()));
        assertFalse(Objects.isNull(deserializedValue.getUpdatedDate()));
    }

}
