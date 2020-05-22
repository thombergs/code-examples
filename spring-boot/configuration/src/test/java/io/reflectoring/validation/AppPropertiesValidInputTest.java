package io.reflectoring.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.properties.name=My Test App",
        "app.properties.report.send-emails=true",
        "app.properties.report.type=PLAIN_TEXT",
        "app.properties.report.interval-in-days=14",
        "app.properties.report.email-address=manager@analysisapp.com"
}, classes = {AppConfiguration.class, AppProperties.class, ReportProperties.class})
class AppPropertiesValidInputTest {

    @Autowired
    AppProperties appProperties;

    @Test
    void propertiesAreLoaded() {
        assertThat(appProperties).isNotNull();
        assertThat(appProperties.getName()).isEqualTo("My Test App");
        assertThat(appProperties.getReport()).isNotNull();
        assertThat(appProperties.getReport().getSendEmails()).isTrue();
        assertThat(appProperties.getReport().getType()).isEqualTo(ReportType.PLAIN_TEXT);
        assertThat(appProperties.getReport().getIntervalInDays()).isEqualTo(14);
        assertThat(appProperties.getReport().getEmailAddress()).isEqualTo("manager@analysisapp.com");
    }

}
