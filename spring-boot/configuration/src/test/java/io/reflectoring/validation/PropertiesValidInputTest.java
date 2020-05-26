package io.reflectoring.validation;

import io.reflectoring.validation.thirdparty.ThirdPartyComponentProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.properties.name=My Test Application",
        "app.properties.report.send-emails=true",
        "app.properties.report.type=PLAIN_TEXT",
        "app.properties.report.interval-in-days=14",
        "app.properties.report.email-address=manager@analysisapp.com",
        "app.third-party.properties.name=Third Party!"
}, classes = {AppConfiguration.class})
class PropertiesValidInputTest {

    @Autowired
    AppProperties appProperties;

    @Autowired
    ThirdPartyComponentProperties thirdPartyComponentProperties;

    @Test
    void appPropertiesAreLoaded() {
        assertThat(appProperties).isNotNull();
        assertThat(appProperties.getName()).isEqualTo("My Test Application");
        assertThat(appProperties.getReport()).isNotNull();
        assertThat(appProperties.getReport().getSendEmails()).isTrue();
        assertThat(appProperties.getReport().getType()).isEqualTo(ReportType.PLAIN_TEXT);
        assertThat(appProperties.getReport().getIntervalInDays()).isEqualTo(14);
        assertThat(appProperties.getReport().getEmailAddress()).isEqualTo("manager@analysisapp.com");
    }

    @Test
    void thirdPartyComponentPropertiesAreLoaded() {
        assertThat(thirdPartyComponentProperties).isNotNull();
        assertThat(thirdPartyComponentProperties.getName()).isEqualTo("Third Party!");
    }

}
