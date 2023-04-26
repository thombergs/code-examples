package com.reflectoring.userdetails.repository;

import com.reflectoring.userdetails.CommonUtil;
import com.reflectoring.userdetails.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
public class DatabaseComponent implements CommandLineRunner {

    private final UserRepository userRepository;

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public DatabaseComponent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        User internalUser1 = new User();
        internalUser1.setAddress("11 Landmark Street, Riverstone");
        internalUser1.setCity("Sydney");
        internalUser1.setDob(CommonUtil.convertToDate(1980, Month.MARCH, 2).format(FORMATTER));
        internalUser1.setSuburb("Riverstone");
        internalUser1.setInternalUser(true);
        internalUser1.setCreatedBy("USER100");
        internalUser1.setCreatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 24));
        internalUser1.setUpdatedBy("USER200");
        internalUser1.setUpdatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 25));
        internalUser1.setFirstName("Rob");
        internalUser1.setLastName("Paulman");
        internalUser1.setLoginId("ROB1908b56");
        internalUser1.setLoginPassword(Base64.getEncoder().encodeToString(
                "LemonMeringue@2023".getBytes(StandardCharsets.UTF_8)));
        internalUser1.setSsnNumber("OVA7890WXFY");
        userRepository.save(internalUser1);

        User internalUser2 = new User();
        internalUser2.setAddress("31 Rocks Street, Kellyville");
        internalUser2.setCity("Sydney");
        internalUser2.setDob(CommonUtil.convertToDate(1983, Month.MAY, 21).format(FORMATTER));
        internalUser2.setSuburb("Kellyville");
        internalUser2.setInternalUser(true);
        internalUser2.setCreatedBy("USER101");
        internalUser2.setCreatedDate(CommonUtil.convertToDate(2023, Month.FEBRUARY, 4));
        internalUser2.setUpdatedBy("USER201");
        internalUser2.setUpdatedDate(CommonUtil.convertToDate(2023, Month.FEBRUARY, 5));
        internalUser2.setFirstName("Linda");
        internalUser2.setLastName("Goodman");
        internalUser2.setLoginId("LIN5670X1");
        internalUser2.setLoginPassword(Base64.getEncoder().encodeToString(
                "Cheesecake@2023".getBytes(StandardCharsets.UTF_8)));
        internalUser2.setSsnNumber("LLH4509KIO");
        userRepository.save(internalUser2);

        User externalUser1 = new User();
        externalUser1.setAddress("126 Grace Parade, Kellyville");
        externalUser1.setCity("Sydney");
        externalUser1.setDob(CommonUtil.convertToDate(1981, Month.JULY, 1).format(FORMATTER));
        externalUser1.setSuburb("Kellyville");
        externalUser1.setInternalUser(false);
        externalUser1.setCreatedBy("USER102");
        externalUser1.setCreatedDate(CommonUtil.convertToDate(2023, Month.FEBRUARY, 14));
        externalUser1.setUpdatedBy("USER202");
        externalUser1.setUpdatedDate(CommonUtil.convertToDate(2023, Month.FEBRUARY, 15));
        externalUser1.setFirstName("Xu");
        externalUser1.setLastName("Ming");
        externalUser1.setLoginId("MIN6780TF");
        externalUser1.setLoginPassword(Base64.getEncoder().encodeToString(
                "RedVelvet@2023".getBytes(StandardCharsets.UTF_8)));
        externalUser1.setSsnNumber("XFDR7891Q");
        userRepository.save(externalUser1);

        User externalUser2 = new User();
        externalUser2.setAddress("11 Whitley Avenue, Bella Vista");
        externalUser2.setCity("Sydney");
        externalUser2.setDob(CommonUtil.convertToDate(1989, Month.SEPTEMBER, 4).format(FORMATTER));
        externalUser2.setSuburb("Bella Vista");
        externalUser2.setInternalUser(false);
        externalUser2.setCreatedBy("USER103");
        externalUser2.setCreatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 24));
        externalUser2.setUpdatedBy("USER203");
        externalUser2.setUpdatedDate(CommonUtil.convertToDate(2023, Month.MARCH, 25));
        externalUser2.setFirstName("Walter");
        externalUser2.setLastName("Hooves");
        externalUser2.setLoginId("WH0078UHI");
        externalUser2.setLoginPassword(Base64.getEncoder().encodeToString(
                "Blueberry@2023".getBytes(StandardCharsets.UTF_8)));
        externalUser2.setSsnNumber("HJYT098UI7");
        userRepository.save(externalUser2);

    }
}
