package io.reflectoring.javaconfig;

import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InfrastructureConfiguration.class})
class InfrastructureConfigurationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void testImportedConfig() {
        // verify that data source is configured
        assertThat(dataSource).as("DataSource should be configured.").isNotNull();

        // verify that cache manager is configured
        assertThat(cacheManager).as("CacheManager should be configured.").isNotNull();

        // verify that security filter chain is configured
        assertThat(securityFilterChain).as("SecurityFilterChain should be configured.").isNotNull();
    }
}