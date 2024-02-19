package io.reflectoring.javaconfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
class DatabaseConfigurationTest {

    private static final String[] DB_PROP_KEYS = {"spring.datasource.driver-class-name", "spring.datasource.url", "spring.datasource.username", "spring.datasource.password"};

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Test
    void dataSource() {
        // verify that data source is configured
        assertThat(dataSource).as("DataSource should be configured.").isNotNull();

        final Properties applicationProperties = propertiesUtil.loadApplicationProperties();
        assertThat(applicationProperties).isNotNull();
        assertThatCode(() -> {
            final Connection connection = dataSource.getConnection();
            final DatabaseMetaData metaData = connection.getMetaData();

            // verify URL
            final String urlKey = "spring.datasource.url";
            assertThat(metaData.getURL()).as("Property %s value should be provided.", "URL").isNotNull();
            assertThat(applicationProperties.containsKey(urlKey)).as("Application property %s should be present.", urlKey)
                                                                 .isTrue();
            final String appUrlValue = applicationProperties.getProperty(urlKey);
            assertThat(appUrlValue).as("Application property %s value should be provided.", urlKey).isNotNull();
            final String[] connectionUrlParts = appUrlValue.split(";");
            assertThat(connectionUrlParts).as("Datasource URL value should be equal to application property %s.", urlKey)
                                          .contains(metaData.getURL());
        }).doesNotThrowAnyException();
    }
}