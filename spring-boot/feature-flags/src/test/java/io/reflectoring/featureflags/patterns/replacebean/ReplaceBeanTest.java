package io.reflectoring.featureflags.patterns.replacebean;

import io.reflectoring.featureflags.FeatureFlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ReplaceBeanTest {

    @MockBean
    private FeatureFlagService featureFlagService;

    @Autowired
    private Service service;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(featureFlagService);
    }

    @Test
    void oldServiceTest() {
        given(featureFlagService.isNewServiceEnabled()).willReturn(false);
        assertThat(service.doSomething()).isEqualTo(1);
    }

    @Test
    void newServiceTest() {
        given(featureFlagService.isNewServiceEnabled()).willReturn(true);
        assertThat(service.doSomething()).isEqualTo(42);
    }

}
