package com.reflectoring.util;

import com.reflectoring.userdetails.CommonUtil;
import com.reflectoring.userdetails.model.UserData;

import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class MockedUsersUtility {

    public static UserData getMockedUserData() {
        UserData mockedUser = new UserData();
        mockedUser.setAddress("11 Landmark Street, Riverstone");
        mockedUser.setCity("Sydney");
        mockedUser.setDob(CommonUtil.convertToDate(1980, Month.MARCH, 2)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mockedUser.setSuburb("Riverstone");
        mockedUser.setInternalUser(true);
        mockedUser.setCreatedBy("USER100");
        mockedUser.setCreatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 24));
        mockedUser.setUpdatedBy("USER200");
        mockedUser.setUpdatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 25));
        mockedUser.setFirstName("Rob");
        mockedUser.setLastName("Paulman");
        mockedUser.setLoginId("ROB1908b56");
        mockedUser.setLoginPassword(Base64.getEncoder().encodeToString(
                "LemonMeringue@2023".getBytes(StandardCharsets.UTF_8)));
        mockedUser.setSsnNumber("OVA7890WXFY");

        return mockedUser;
    }

    public static String userDataObjectAsString() {
        return "{\"id\":0,\"firstName\":\"Rob\",\"lastName\":\"Paulman\",\"dob\":\"1980-03-02\",\"address\":\"11 Landmark Street, Riverstone\",\"suburb\":\"Riverstone\",\"city\":\"Sydney\",\"additionalData\":null,\"loginId\":\"ROB1908b56\",\"loginPassword\":\"afgr5thrf\",\"createdBy\":\"USER100\",\"createdDate\":[2023,3,24],\"updatedBy\":\"USER200\",\"updatedDate\":[2023,3,25]}\n";
    }
}
