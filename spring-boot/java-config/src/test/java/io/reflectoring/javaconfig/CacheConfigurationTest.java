package io.reflectoring.javaconfig;

import io.reflectoring.javaconfig.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppCacheConfiguration.class, JavaConfigAppConfiguration.class})
class CacheConfigurationTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JavaConfigAppConfiguration appConfiguration;

    @Test
    void employeesAreSame() {
        final String firstName = "Foo";
        final String lastName = "Bar";
        final Employee employee = appConfiguration.newEmployee(firstName, lastName);
        final Employee cachedEmployee = appConfiguration.newEmployee(firstName, lastName);
        assertThat(employee).as("Both employees should be same.")
                            .isSameAs(cachedEmployee);
    }

    @Test
    void employeesAreNotSame() {
        final Employee employee = appConfiguration.newEmployee("Foo", "Bar");
        final Employee secondEmployee = appConfiguration.newEmployee("Zoo", "Bar");
        assertThat(employee).as("Both employees should not be same.")
                            .isNotSameAs(secondEmployee);
    }
}