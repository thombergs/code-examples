package io.reflectoring.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class DateTimeServicesTest {

    @Test
    void getMax() {
        DateTimeServices dateTimeServices = new DateTimeServices();
        assertThat(dateTimeServices.getMax()).isEqualTo(BigDecimal.ZERO);
    }
}