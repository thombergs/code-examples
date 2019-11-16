package io.reflectoring.staticdata;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("static")
public class QuotesProperties {

    private final List<Quote> quotes;

    public QuotesProperties(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public List<Quote> getQuotes() {
        return this.quotes;
    }

}
