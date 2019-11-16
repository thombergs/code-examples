package io.reflectoring.staticdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.location = ./,file:./quotes.yml"})
class QuotesPropertiesTest {

    @Autowired
    private QuotesProperties quotesProperties;

    @Test
    void staticQuotesAreLoaded() {
        assertThat(quotesProperties.getQuotes()).hasSize(2);
    }

}