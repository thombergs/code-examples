package io.reflectoring.featureflags.patterns.replacemethod;

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
public class ReplaceMethodTest {

    @MockBean
    private FeatureFlagService featureFlagService;

    @Autowired
    private Service service;

    @Autowired
    private OldService oldService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(featureFlagService);
    }

    @Test
    void oldServiceTest() {
        given(featureFlagService.isNewServiceEnabled()).willReturn(false);
        assertThat(service.doSomething()).isEqualTo(1);
        assertThat(oldService.doSomethingElse()).isEqualTo(2);
    }

    @Test
    void newServiceTest() {
        given(featureFlagService.isNewServiceEnabled()).willReturn(true);
        assertThat(service.doSomething()).isEqualTo(42);
        // doSomethingElse() is not behind a feature flag, so it should return the same value independant of the feature flag
        assertThat(oldService.doSomethingElse()).isEqualTo(2);
    }

}
