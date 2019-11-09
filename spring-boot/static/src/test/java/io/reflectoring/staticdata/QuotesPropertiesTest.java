package io.reflectoring.staticdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// @TestPropertySource cannot load YML files directly (using the "locations" attribute)
// so we simply tell Spring Boot to load the properties via spring.config.location.
@TestPropertySource(properties = { "spring.config.location = file:./quotes.yml" })
class QuotesPropertiesTest {

    @Autowired
    private QuotesProperties quotesProperties;

    @Test
    void staticQuotesAreLoaded() {
        assertThat(quotesProperties.getQuotes()).hasSize(2);
    }

}