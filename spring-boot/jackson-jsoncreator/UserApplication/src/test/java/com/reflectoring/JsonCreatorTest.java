package com.reflectoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.model.UserType;
import com.reflectoring.userdetails.model.UserTypeData;
import com.reflectoring.userdetails.persistence.User;
import com.reflectoring.userdetails.service.CommonBean;
import com.reflectoring.util.MockedUsersUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static com.reflectoring.util.MockedUsersUtility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CommonBean.class)
@RunWith(SpringRunner.class)
public class JsonCreatorTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testMapper() {
        assertNotNull(objectMapper);
    }

    @Test
    public void deserializeImmutableObjects() throws JsonProcessingException {
        String userData = objectMapper.writeValueAsString(MockedUsersUtility.getMockedUserData());
        System.out.println("USER: " + userData);
        UserData user = objectMapper.readValue(userData, UserData.class);
        System.out.println("DESERIALISED USER:" + user);
        assertNotNull(user);

    }


    @Test
    public void jsonCreatorWithDelegatingMode() throws JsonProcessingException {
        String userTypeJson = objectMapper.writeValueAsString(getMockedUserTypeData());
        System.out.println("Typed Json: " + userTypeJson);
        assertNotNull(userTypeJson);
        UserTypeData userType = objectMapper.readValue(userTypeJson, UserTypeData.class);
        assertEquals(userType.getUserType().getId(), UserType.EXTERNAL.getId());
    }

    @Test
    public void jsonCreatorWithDelegatingMode2() throws JsonProcessingException {
        String userTypeJson = objectMapper.writeValueAsString(UserType.EXTERNAL);
        System.out.println("Typed Json: " + userTypeJson);
        assertNotNull(userTypeJson);
        UserType userType = objectMapper.readValue(userTypeJson, UserType.class);
        assertEquals(userType.getId(), UserType.EXTERNAL.getId());
    }

    @Test
    public void jsonCreatorWithDelegatingMode3() throws JsonProcessingException {
        String userDataJson = objectMapper.writeValueAsString(getMockedUserDataMap());
        System.out.println("Typed Json Map: " + userDataJson);
        assertNotNull(userDataJson);
        UserData data = objectMapper.readValue(userDataJson, UserData.class);
    }

}
