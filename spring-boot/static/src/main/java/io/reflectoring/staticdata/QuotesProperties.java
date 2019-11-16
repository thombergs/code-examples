package io.reflectoring.staticdata;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("static")
class QuotesProperties {

    private final List<Quote> quotes;

    QuotesProperties(List<Quote> quotes) {
        this.quotes = quotes;
    }

    List<Quote> getQuotes() {
        return this.quotes;
    }

}
